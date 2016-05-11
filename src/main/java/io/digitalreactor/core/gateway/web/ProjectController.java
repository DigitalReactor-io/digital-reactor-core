package io.digitalreactor.core.gateway.web;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

/**
 * Created by ingvard on 08.05.16.
 */
public class ProjectController {

    private Vertx vertx;
    private Router router;
    private HandlebarsTemplateEngine engine;
    private HttpClient httpClient;
    private EventBus eventBus;

    public ProjectController(Vertx vertx, HandlebarsTemplateEngine engine) {
        this.vertx = vertx;
        this.engine = engine;
        this.eventBus = vertx.eventBus();
        this.httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setKeepAlive(false));

        router = Router.router(vertx);

        router.route(HttpMethod.GET, "/").handler(this::projectList);
        router.route(HttpMethod.GET, "/:id/").handler(this::project);
    }

    public Router router() {
        return router;
    }

    private void project(RoutingContext routingContext) {
        engine.render(routingContext, "src/main/webapp/project.hbs", res -> {
            if (res.succeeded()) {
                routingContext.response().end(res.result());
            } else {
                routingContext.fail(res.cause());
            }
        });
    }

    private void projectList(RoutingContext routingContext) {
        engine.render(routingContext, "src/main/webapp/project-list.hbs", res -> {
            if (res.succeeded()) {
                routingContext.response().end(res.result());
            } else {
                routingContext.fail(res.cause());
            }
        });
    }
}
