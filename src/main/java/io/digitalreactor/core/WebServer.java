package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;

/**
 * Created by ingvard on 03.04.16.
 */
public class WebServer extends AbstractVerticle {

    public void start() throws Exception {
        HttpServer httpServer = vertx.createHttpServer();
        EventBus eventBus = vertx.eventBus();
        Router router = Router.router(vertx);

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
