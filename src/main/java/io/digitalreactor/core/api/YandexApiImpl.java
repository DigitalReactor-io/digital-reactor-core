package io.digitalreactor.core.api;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public class YandexApiImpl implements YandexApi {

    private final Vertx vertx;

    private final HttpClient httpClient;

    private final String path = "https://api-metrika.yandex.ru";

    private static final String API_HOST = "api-metrika.yandex.ru";

    private final String token;

    public YandexApiImpl(Vertx vertx, String token) {
        this.vertx = vertx;
        this.httpClient = vertx.createHttpClient(
                new HttpClientOptions()
                        .setSsl(true)
                        .setVerifyHost(false)
                        .setDefaultHost(API_HOST)
        );
        this.token = "&oauth_token=" + token;
    }

    @Override
    public String tables(RequestTable requestTable) {
        return executeRequest(requestTable);
    }

    @Override
    public void tables(RequestTable requestTable, Consumer<String> consumer) {
        executeRequest(requestTable, consumer);
    }

    @Override
    public String counters(RequestCounterList requestCounterList) {
        return executeRequest(requestCounterList);
    }

    @Override
    public void counters(RequestCounterList requestCounterList, Consumer<String> consumer) {
        executeRequest(requestCounterList, consumer);
    }

    private String executeRequest(Request request) {
        CompletableFuture<String> future = new CompletableFuture<>();
        httpClient.getAbs(path + request.toQuery() + token, response -> {
            response.bodyHandler(body -> {
                future.complete(body.toString());
            });
        }).end();
        return waitResult(future);
    }

    private void executeRequest(Request request, Consumer<String> consumer) {
        httpClient.getAbs(path + request.toQuery() + token, response -> {
            response.bodyHandler(body -> {
                vertx.executeBlocking(future -> {
                            consumer.accept(body.toString());
                            future.complete();
                        },
                        futureResult -> {
                        });
            });
        }).end();
    }

    private String waitResult(CompletableFuture<String> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }
}
