package io.digitalreactor.core.gateway.web;

import io.digitalreactor.core.api.RequestCounterList;
import io.digitalreactor.core.api.YandexApi;
import io.digitalreactor.core.api.YandexApiImpl;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
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
    private HttpClient httpClient;

    //TODO[St.Maxim] get from env
    private String APPLICATION_ID = "b26e324d5a134168b090b3f23e77a0e7";
    private String APPLICATION_AUTH = "Basic YjI2ZTMyNGQ1YTEzNDE2OGIwOTBiM2YyM2U3N2EwZTc6Yjk3MGJjMWIzOGI3NDE5YWEyN2Y4YjhjM2Q1ZDEzZTA=";
    private String CLIENT_SECRET = "b970bc1b38b7419aa27f8b8c3d5d13e0";

    public RegistrationController(Vertx vertx, HandlebarsTemplateEngine engine) {
        this.vertx = vertx;
        this.engine = engine;
        this.httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setKeepAlive(false));

        router = Router.router(vertx);

        router.route(HttpMethod.GET, "/").handler(this::registration);
        router.route(HttpMethod.POST, "/accessing").handler(this::accessing);
        router.route(HttpMethod.GET, "/choose").handler(this::chooseProject);
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

                    routingContext.put("clientId", APPLICATION_ID);

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

    private void chooseProject(RoutingContext routingContext) {
        String code = routingContext.request().getParam("code");

        String temporaryToken = "grant_type=authorization_code&code=" + code +
                "&client_id=" + APPLICATION_ID + "&client_secret=" + CLIENT_SECRET;

        httpClient.post(443, "oauth.yandex.ru", "/token", httpResponse -> {
            httpResponse.bodyHandler(bufferBody -> {
                if (httpResponse.statusCode() == 200) {
                    String accessToken = bufferBody.toJsonObject().getString("access_token");

                   YandexApi yandexApi = new YandexApiImpl(vertx, accessToken);
                    String json = yandexApi.counters(RequestCounterList.of().build());

                    //TODO[St.maxim] get counter list
                }
            });

            //TODO[St.maxim] 3DES for token
            //TODO[St.maxim] logger - error
        }).putHeader("Authorization", APPLICATION_AUTH).end(temporaryToken);
    }
}
