package io.digitalreactor.core;

import io.digitalreactor.core.application.UserManagerAuthProvider;
import io.digitalreactor.core.gateway.web.ProjectController;
import io.digitalreactor.core.gateway.web.RegistrationController;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

/**
 * Created by ingvard on 03.04.16.
 */
public class WebServer extends AbstractVerticle {

    private final static HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create();

    public void start() throws Exception {

        final Router router = Router.router(vertx);

        router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));

        //TODO[St.Maxim] Mb point to send, but not vertex
        AuthProvider authProvider = new UserManagerAuthProvider(vertx.eventBus(), UserManagerVerticle.AUTHENTICATE);

        router.route().handler(UserSessionHandler.create(authProvider));

        router.route("/project/*").handler(RedirectAuthHandler.create(authProvider, "/loginpage"));

        router.route("/logon").handler(FormLoginHandler.create(authProvider));

        router.route("/logout").handler(context -> {
            context.clearUser();
            context.response().putHeader("location", "/").setStatusCode(302).end();
        });

        router.route("/js/*").handler(StaticHandler.create("src/main/webapp/js"));
        router.route("/fonts/*").handler(StaticHandler.create("src/main/webapp/fonts"));
        router.route("/css/*").handler(StaticHandler.create("src/main/webapp/css"));
        router.route("/images/*").handler(StaticHandler.create("src/main/webapp/images"));

        router.mountSubRouter("/registration/", new RegistrationController(vertx, engine).router());
        router.mountSubRouter("/project/", new ProjectController(vertx, engine).router());

        router.route("/loginpage").handler(ctx -> {
            engine.render(ctx, "src/main/webapp/loginpage.hbs", res -> {
                if (res.succeeded()) {
                    ctx.response().end(res.result());
                } else {
                    ctx.fail(res.cause());
                }
            });
        });

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
