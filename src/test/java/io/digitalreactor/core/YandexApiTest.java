package io.digitalreactor.core;

import io.digitalreactor.core.api.yandex.YandexApi;
import io.digitalreactor.core.api.yandex.YandexApiImpl;
import io.digitalreactor.core.api.yandex.model.Request;
import io.digitalreactor.core.api.yandex.model.RequestCounters;
import io.digitalreactor.core.api.yandex.model.Response;
import io.digitalreactor.core.promise.oncontext.Promise;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static io.digitalreactor.core.api.yandex.model.AbstractRequest.COUNTETS;
import static io.digitalreactor.core.api.yandex.model.AbstractRequest.DATA;
import static io.digitalreactor.core.api.yandex.model.AbstractRequest.DATA_BY_TYPE;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
@RunWith(VertxUnitRunner.class)
public class YandexApiTest {

    private String token = "ARRC9_4AAr_pAYPQUd4tTruTtGS8ouTlHg";

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();

    @Test
    public void request_allCounters_counters(TestContext context) {
        YandexApi yandexApi = new YandexApiImpl(rule.vertx());

        Async async = context.async();

        Promise.onContext(rule.vertx())
                .when((Future<JsonObject> f) -> {
                    RequestCounters requestCounters = RequestCounters.of()
                            .token(token)
                            .prefix(COUNTETS)
                            .build();
                    yandexApi.requestAsJson(requestCounters, f);
                })
                .then(response -> {
                    System.out.println(response);
                    context.assertNotNull(response);
                    async.complete();
                });
    }


    @Test
    public void request_visits_visitsDuringMonth(TestContext context) {
        YandexApi yandexApi = new YandexApiImpl(rule.vertx());

        Async async = context.async();

        Promise.onContext(rule.vertx())
                .when((Future<JsonObject> f) -> {
                    Request request = Request.of()
                            .prefix(DATA_BY_TYPE)
                            .ids("29235010")
                            .metrics("ym:s:visits")
                            .date1("2015-01-01")
                            .date2("2015-01-28")
                            .group("day")
                            .pretty(true)
                            .token(token)
                            .build();

                    yandexApi.requestAsJson(request, f.completer());
                })
                .then(response -> {
                    System.out.println(response);
                    context.assertNotNull(response);
                    async.complete();
                });
    }

    @Test
    public void request_filterByTrafficSource_referenceSourcesDuringMonth(TestContext context) {
        YandexApi yandexApi = new YandexApiImpl(rule.vertx());

        Async async = context.async();

        Promise.onContext(rule.vertx())
                .when((Future<JsonObject> f) -> {
                    Request request = Request.of()
                            .prefix(DATA_BY_TYPE)
                            .ids("29235010")
                            .metrics("ym:s:visits")
                            .date1("2015-06-01")
                            .date2("2015-06-28")
                            .group("day")
                            .dimensions("ym:s:<attribution>TrafficSource")
                            .attribution("last")
                            .pretty(true)
                            .token(token)
                            .build();

                    yandexApi.requestAsJson(request, f.completer());
                })
                .then(response -> {
                    System.out.println(response);
                    context.assertNotNull(response);
                    async.complete();
                });
    }

    @Test
    public void request_searchPhrase_searchPhraseDuringMonth(TestContext context) {
        YandexApi yandexApi = new YandexApiImpl(rule.vertx());

        Async async = context.async();

        Promise.onContext(rule.vertx())
                .when((Future<JsonObject> f) -> {
                    Request request = Request.of()
                            .prefix(DATA)
                            .ids("29235010")
                            .preset("sources_search_phrases")
                            .date1("2015-06-01")
                            .date2("2015-06-28")
                            .token(token)
                            .build();

                    yandexApi.requestAsJson(request, f.completer());
                })
                .then(response -> {
                    System.out.println(response);
                    context.assertNotNull(response);
                    async.complete();
                });
    }

}
