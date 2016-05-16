package io.digitalreactor.core;

import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by flaidzeres on 13.05.2016.
 */
@RunWith(VertxUnitRunner.class)
public class IntegrationTests {
    @Test
    public void test(TestContext context) {
        String main = "result";

        Async async = context.async();
        Future f1 = Future.future();
        Future f2 = Future.future();
        Future f3 = Future.future();

        Vertx vertx = Vertx.vertx();

        SummaryDispatcherVerticle summaryDispatcherVerticle = new SummaryDispatcherVerticle();
        SummaryStorageVerticle summeryStorageVerticle = new SummaryStorageVerticle();
        MetricsLoaderVerticle metricsLoaderVerticle = new MetricsLoaderVerticle();

        vertx.deployVerticle(summeryStorageVerticle, s -> {
            f1.complete();
        });
        vertx.deployVerticle(summaryDispatcherVerticle, s -> {
            f2.complete();
        });
        vertx.deployVerticle(metricsLoaderVerticle, s -> {
            f3.complete();
        });

        CompositeFuture.all(f1, f2, f3).setHandler(f -> {
            if (f.succeeded()) {
                CreateSummaryMessage summaryMessage = new CreateSummaryMessage(
                        "ARRC9_4AAr_pAYPQUd4tTruTtGS8ouTlHg",
                        "29235010",
                        "77",
                        Stream.of(ReportTypeEnum.VISITS_DURING_MONTH).collect(Collectors.toList()),
                        Stream.of(main).collect(Collectors.toList())
                );
                vertx.eventBus().send(SummaryDispatcherVerticle.CREATE_SUMMARY, ReactorAbstractVerticle.toJson(summaryMessage));
            }
        });

        vertx.eventBus().consumer(main, msg -> {
            System.out.println("summary is ready [" + msg.body() + "]");
            async.complete();
        });

    }

}
