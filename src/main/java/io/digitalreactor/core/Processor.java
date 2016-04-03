package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by ingvard on 03.04.16.
 */
public class Processor extends AbstractVerticle {

    public static final String CALCULATE = "core.processor.calculate";

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(CALCULATE, context -> {

        });
    }
}
