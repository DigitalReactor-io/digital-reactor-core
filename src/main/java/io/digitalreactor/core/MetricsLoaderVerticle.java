package io.digitalreactor.core;

import io.digitalreactor.core.api.RequestTable;
import io.digitalreactor.core.api.YandexApi;
import io.digitalreactor.core.api.YandexApiImpl;
import io.digitalreactor.core.domain.MetricsLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * Created by ingvard on 07.04.16.
 */
public class MetricsLoaderVerticle extends AbstractVerticle {

    public static final String LOAD_REPORT = "report.loader.load_report";

    private MetricsLoader metricsLoader;

    private EventBus eventBus;

    private YandexApi yandexApi;

    @Override
    public void start() throws Exception {
        this.eventBus = vertx.eventBus();
        this.metricsLoader = new MetricsLoader();
        this.yandexApi = new YandexApiImpl(vertx, "");
        eventBus.consumer(LOAD_REPORT, this::loadReport);
    }

    private <T> void loadReport(Message<T> message) {

        //todo map input message to normal request for yandex api
        RequestTable requestTable = RequestTable.of()
                .directClientLogins("directId1", "directId2")
                .ids("id1", "id2", "id3")
                .date1("fromDay")
                .date2("toDay")
                .dimensions("dimensions1")
                .metrics("metrics1")
                .offset(3)
                .pretty(true)
                .build();

        yandexApi.tables(requestTable, result -> {
            eventBus.publish("", result);
        });

    }
}
