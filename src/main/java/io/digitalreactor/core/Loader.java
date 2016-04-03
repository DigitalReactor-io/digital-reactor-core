package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;

/**
 * Created by ingvard on 03.04.16.
 */
public class Loader extends AbstractVerticle {

    public static final String LOADER_RUN = "core.loader";

    @Override
    public void start() throws Exception {
        HttpClient httpClient = vertx.createHttpClient();
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(LOADER_RUN, context -> {
            Object result = new Object();
            eventBus.publish(Processor.CALCULATE, result);
        });
    }
}
