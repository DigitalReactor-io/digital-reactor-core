package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

/**
 * Created by ingvard on 03.04.16.
 */
public class WebServer extends AbstractVerticle {

    private final static HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

    public void start() throws Exception {

        final Router router = Router.router(vertx);

        router.get().handler(ctx -> {
            ctx.put("name", "test");
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
