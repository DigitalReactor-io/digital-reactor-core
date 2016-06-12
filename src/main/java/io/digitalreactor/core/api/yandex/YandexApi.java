package io.digitalreactor.core.api.yandex;

import io.digitalreactor.core.api.yandex.model.Request;
import io.digitalreactor.core.api.yandex.model.RequestCounters;
import io.digitalreactor.core.api.yandex.model.Response;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public interface YandexApi {

    void request(RequestCounters request, Handler<AsyncResult<Response>> resultHandler);

    void requestAsString(RequestCounters request, Handler<AsyncResult<String>> resultHandler);

    void requestAsJson(RequestCounters request, Handler<AsyncResult<JsonObject>> resultHandler);

    void request(Request request, Handler<AsyncResult<Response>> resultHandler);

    void requestAsString(Request request, Handler<AsyncResult<String>> resultHandler);

    void requestAsJson(Request request, Handler<AsyncResult<JsonObject>> resultHandler);

}
