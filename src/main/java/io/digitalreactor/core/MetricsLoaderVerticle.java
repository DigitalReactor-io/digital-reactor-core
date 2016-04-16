package io.digitalreactor.core;

import io.digitalreactor.core.domain.MetricsLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;

/**
 * Created by ingvard on 07.04.16.
 */
public class MetricsLoaderVerticle extends AbstractVerticle {

    public static final String LOAD_REPORT = "report.loader.load_report";

    private static final String API_HOST = "api-metrika.yandex.ru";

    private HttpClient httpClient;
    private MetricsLoader metricsLoader;

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        httpClient = vertx.createHttpClient();
        metricsLoader = new MetricsLoader();

        eventBus.consumer(LOAD_REPORT, this::loadReport);
    }

    private <T> void loadReport(Message<T> message) {

      //  String requestURI = metricsLoader.resolveUri();

        //TODO[st.maxim] need to fix callback hell
     /*   httpClient.get(443, API_HOST, requestURI, response -> {
            if (response.statusCode() == 200) {
                response.bodyHandler(responseBody -> {
                    responseBody.toString("UTF-8");
                });
            }

            //message.fail();
            //TODO[st.maxim] errorHandler

        });*/

    }
}
