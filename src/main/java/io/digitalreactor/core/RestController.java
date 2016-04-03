package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;

/**
 * Created by ingvard on 03.04.16.
 */
public class RestController extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        EventBus eventBus = vertx.eventBus();

        httpServer.requestHandler(context -> {

            Object message = new Object();
            eventBus.publish(Processor.CALCULATE, message);

            context.response().end("Ok");

        }).listen(8080);
    }
}
