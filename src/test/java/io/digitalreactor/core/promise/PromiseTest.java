package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.oncontext.Promise;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@RunWith(VertxUnitRunner.class)
public class PromiseTest extends AbstractPromiseTest {

    @Test
    public void test(TestContext testContext) {
        Async async = testContext.async();
        Vertx vertx = rule.vertx();

        Promise.onContext(vertx)
                .when((Future<Void> f) -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("start");
                    f.complete();
                })
                .andThenBlocking(() -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("next");
                })
                .andThen((Future<String> f) -> {
                    System.out.println(Thread.currentThread().getName());
                    vertx.deployVerticle(new MyVerticle(), f.completer());
                })
                .then(id -> {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println("end id:" + id);
                    async.complete();
                });


    }


}
