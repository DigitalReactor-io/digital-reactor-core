package io.digitalreactor.core.api.yandex;

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

    private final String tokenPrefix;

    public YandexApiImpl(Vertx vertx) {
        this.vertx = vertx;
        this.httpClient = vertx.createHttpClient(
                new HttpClientOptions()
                        .setSsl(true)
                        .setVerifyHost(false)
                        .setDefaultHost(API_HOST)
        );
        this.tokenPrefix = "oauth_token=";
    }

    @Override
    public String tables(RequestTable requestTable, String token) {
        return executeRequest(requestTable, token);
    }

    @Override
    public void tables(RequestTable requestTable, String token, Consumer<String> consumer) {
        executeRequest(requestTable, token, consumer);
    }

    @Override
    public String counters(RequestCounterList requestCounterList, String token) {
        return executeRequest(requestCounterList, token);
    }

    @Override
    public void counters(RequestCounterList requestCounterList, String token, Consumer<String> consumer) {
        executeRequest(requestCounterList, token, consumer);
    }

    private String executeRequest(Request request, String token) {
        CompletableFuture<String> future = new CompletableFuture<>();
        httpClient.getAbs(path + request.prefix() + tokenPrefix + token + request.toQuery(), response -> {
            response.bodyHandler(body -> {
                future.complete(body.toString());
            });
        }).end();
        return waitResult(future);
    }

    private void executeRequest(Request request, String token, Consumer<String> consumer) {
        httpClient.getAbs(path + request.prefix() + tokenPrefix + token + request.toQuery(), response -> {
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
