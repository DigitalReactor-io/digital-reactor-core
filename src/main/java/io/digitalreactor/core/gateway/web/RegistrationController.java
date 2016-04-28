package io.digitalreactor.core.gateway.web;

import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;


/**
 * Created by ingvard on 27.04.16.
 */
public class RegistrationController {

    private Vertx vertx;
    private Router router;
    private HandlebarsTemplateEngine engine;

    public RegistrationController(Vertx vertx, HandlebarsTemplateEngine engine) {
        this.vertx = vertx;
        this.engine = engine;

        router = Router.router(vertx);

        router.route(HttpMethod.GET, "/").handler(this::registration);
        router.route(HttpMethod.POST, "/accessing").handler(this::accessing);

    }

    public Router router() {
        return router;
    }

    private void registration(RoutingContext routingContext) {
        engine.render(routingContext, "src/main/webapp/registration-step-1.hbs", res -> {
            if (res.succeeded()) {
                routingContext.response().end(res.result());
            } else {
                routingContext.fail(res.cause());
            }
        });
    }

    private void accessing(RoutingContext routingContext) {
        routingContext.request().bodyHandler(bodyBuffer -> {
                    QueryStringDecoder qsd = new QueryStringDecoder(bodyBuffer.toString(), false);
                    String email = qsd.parameters().get("email").get(0);
                    routingContext.addCookie(Cookie.cookie("email", email).setMaxAge(1000).setHttpOnly(true));

                    engine.render(routingContext, "src/main/webapp/registration-step-2.hbs", res -> {
                        if (res.succeeded()) {

                            routingContext.response().end(res.result());
                        } else {
                            routingContext.fail(res.cause());
                        }
                    });
                }
        );
    }
}
