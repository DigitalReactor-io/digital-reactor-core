package io.digitalreactor.core;

import io.digitalreactor.core.gateway.web.RegistrationController;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

/**
 * Created by ingvard on 03.04.16.
 */
public class WebServer extends AbstractVerticle {

    private final static HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

    public void start() throws Exception {

        final Router router = Router.router(vertx);

        router.route().handler(CookieHandler.create());

        router.route("/js/*").handler(StaticHandler.create("src/main/webapp/js"));
        router.route("/fonts/*").handler(StaticHandler.create("src/main/webapp/fonts"));
        router.route("/css/*").handler(StaticHandler.create("src/main/webapp/css"));
        router.route("/images/*").handler(StaticHandler.create("src/main/webapp/images"));

        router.mountSubRouter("/registration/", new RegistrationController(vertx, engine).router());


        router.get().handler(ctx -> {
            engine.render(ctx, "src/main/webapp/index.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    }
}
