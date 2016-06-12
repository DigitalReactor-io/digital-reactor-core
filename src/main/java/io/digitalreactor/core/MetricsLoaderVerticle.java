package io.digitalreactor.core;

import io.digitalreactor.core.api.yandex.YandexApi;
import io.digitalreactor.core.api.yandex.YandexApiImpl;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.promise.oncontext.Promise;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

import static io.digitalreactor.core.ReportCreatorVerticle.CREATE_REPORT;
import static io.digitalreactor.core.api.yandex.RequestBuilder.createRequest;

/**
 * Created by ingvard on 07.04.16.
 */
public class MetricsLoaderVerticle extends ReactorAbstractVerticle {

    public static final String LOAD_REPORT = "report.loader.load_report";

    @Override
    public void start() throws Exception {
        YandexApi yandexApi = new YandexApiImpl(vertx);
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(LOAD_REPORT, result -> {
            ReportMessage message = toObj((String) result.body(), ReportMessage.class);

            Promise.onContext(vertx)
                    .when((Future<String> f) -> {
                        yandexApi.requestAsString(createRequest(message), f.completer());
                    })
                    .then(response -> {
                        message.raw = response;
                        eventBus.publish(CREATE_REPORT, message);
                    });
        });
    }

}
