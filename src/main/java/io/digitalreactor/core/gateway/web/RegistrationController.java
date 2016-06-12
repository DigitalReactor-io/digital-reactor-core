package io.digitalreactor.core.gateway.web;

import io.digitalreactor.core.UserManagerVerticle;
import io.digitalreactor.core.api.yandex.YandexApi;
import io.digitalreactor.core.api.yandex.YandexApiImpl;
import io.digitalreactor.core.api.yandex.model.RequestCounters;
import io.digitalreactor.core.gateway.web.dto.CounterShortDto;
import io.digitalreactor.core.promise.oncontext.Promise;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.HandlebarsTemplateEngine;

import java.util.*;


/**
 * Created by ingvard on 27.04.16.
 */
public class RegistrationController {

    private Vertx vertx;
    private Router router;
    private HandlebarsTemplateEngine engine;
    private HttpClient httpClient;
    private EventBus eventBus;

    //TODO[St.Maxim] get from env
    private String APPLICATION_ID = "b26e324d5a134168b090b3f23e77a0e7";
    private String APPLICATION_AUTH = "Basic YjI2ZTMyNGQ1YTEzNDE2OGIwOTBiM2YyM2U3N2EwZTc6Yjk3MGJjMWIzOGI3NDE5YWEyN2Y4YjhjM2Q1ZDEzZTA=";
    private String CLIENT_SECRET = "b970bc1b38b7419aa27f8b8c3d5d13e0";

    //TODO[St.Maxim] size limit
    private Map<String, String> temporaryTokenStorage = new HashMap<>();

    public RegistrationController(Vertx vertx, HandlebarsTemplateEngine engine) {
        this.vertx = vertx;
        this.engine = engine;
        this.eventBus = vertx.eventBus();
        this.httpClient = vertx.createHttpClient(new HttpClientOptions()
                .setSsl(true)
                .setTrustAll(true)
                .setKeepAlive(false));

        router = Router.router(vertx);

        router.route(HttpMethod.GET, "/").handler(this::registration);
        router.route(HttpMethod.POST, "/accessing").handler(this::accessing);
        router.route(HttpMethod.GET, "/choose").handler(this::chooseProject);
        router.route(HttpMethod.POST, "/finish").handler(this::finish);
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
        MultiMap params = routingContext.request().formAttributes();
        String email = params.get("email");

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

    private void chooseProject(RoutingContext routingContext) {
        String code = routingContext.request().getParam("code");

        String temporaryToken = "grant_type=authorization_code&code=" + code +
                "&client_id=" + APPLICATION_ID + "&client_secret=" + CLIENT_SECRET;

        //TODO[St.maxim] callback hell and test hell too:)
        httpClient.post(443, "oauth.yandex.ru", "/token", httpResponse -> {
            httpResponse.bodyHandler(bufferBody -> {
                if (httpResponse.statusCode() == 200) {
                    String accessToken = bufferBody.toJsonObject().getString("access_token");

                    String tokenCode = UUID.randomUUID().toString();
                    routingContext.addCookie(Cookie.cookie("token", tokenCode).setMaxAge(1000).setHttpOnly(true));
                    temporaryTokenStorage.put(tokenCode, accessToken);

                    YandexApi yandexApi = new YandexApiImpl(vertx);

                    Promise.onContext(vertx)
                            .when((Future<JsonObject> f) -> {
                                yandexApi.requestAsJson(
                                        RequestCounters.of().token(accessToken).build(),
                                        f.completer()
                                );
                            })
                            .then(response -> {
                                JsonArray counters = response.getJsonArray("counters");
                                List<CounterShortDto> countersDto = new ArrayList<CounterShortDto>();

                                for (Object counter : counters) {
                                    Long id = ((JsonObject) counter).getLong("id");
                                    String name = ((JsonObject) counter).getString("name");

                                    countersDto.add(new CounterShortDto(id, name));
                                }

                                routingContext.put("counters", countersDto);

                                engine.render(routingContext, "src/main/webapp/registration-step-3.hbs", res -> {
                                    if (res.succeeded()) {
                                        routingContext.response().end(res.result());
                                    } else {
                                        routingContext.fail(res.cause());
                                    }
                                });
                            });
                }
            });

            //TODO[St.maxim] 3DES for token
            //TODO[St.maxim] logger - error
        }).putHeader("Authorization", APPLICATION_AUTH).end(temporaryToken);
    }

    private void finish(RoutingContext routingContext) {
        MultiMap params = routingContext.request().formAttributes();
        String email = routingContext.getCookie("email").getValue();
        //TODO [St.Maxim] Need to remove the getting token && logger - null result
        String token = temporaryTokenStorage.get(routingContext.getCookie("token").getValue());

        String counterId = params.get("counterId");
        String name = params.get("name");

        //TODO[St.maxim] mb need more strict typesafe a object structure? Dto?
        JsonObject createNewUserObj = new JsonObject()
                .put("email", email)
                .put("token", token)
                .put("counterId", counterId)
                .put("name", name);

        eventBus.send(UserManagerVerticle.NEW_USER, createNewUserObj, reply -> {
            if (reply.succeeded()) {
                routingContext.response().setStatusCode(201).end();
            } else {
                routingContext.response().setStatusCode(500).end();
            }
        });


    }
}
