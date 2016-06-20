package io.digitalreactor.core.api.yandex;

import io.digitalreactor.core.api.yandex.model.AbstractRequest;
import io.digitalreactor.core.api.yandex.model.Request;
import io.digitalreactor.core.api.yandex.model.RequestCounters;
import io.digitalreactor.core.api.yandex.model.Response;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * Created by FlaIDzeres on 23.04.2016.
 */
public class YandexApiImpl implements YandexApi {

    private final ObjectMapper mapper = JsonFactory.create();

    private final HttpClient httpClient;

    private final String apiPath = "https://api-metrika.yandex.ru";

    public YandexApiImpl(Vertx vertx) {
        this.httpClient = vertx.createHttpClient(
                new HttpClientOptions()
                        .setSsl(true)
                        .setVerifyHost(false)
                        .setTrustAll(true)
        );
    }

    @Override
    public void request(RequestCounters request, Handler<AsyncResult<Response>> resultHandler) {
        execute(request, buffer -> {
            final Response response = mapper.fromJson(buffer.toString(), Response.class);
            resultHandler.handle(new AsyncResult<Response>() {
                @Override
                public Response result() {
                    return response;
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    @Override
    public void requestAsString(RequestCounters request, Handler<AsyncResult<String>> resultHandler) {
        execute(request, buffer -> {
            resultHandler.handle(new AsyncResult<String>() {
                @Override
                public String result() {
                    return buffer.toString();
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    @Override
    public void requestAsJson(RequestCounters request, Handler<AsyncResult<JsonObject>> resultHandler) {
        execute(request, buffer -> {
            resultHandler.handle(new AsyncResult<JsonObject>() {
                @Override
                public JsonObject result() {
                    return buffer.toJsonObject();
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    @Override
    public void request(Request request, Handler<AsyncResult<Response>> resultHandler) {
        execute(request, buffer -> {
            final Response response = mapper.fromJson(buffer.toString(), Response.class);
            resultHandler.handle(new AsyncResult<Response>() {
                @Override
                public Response result() {
                    return response;
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    @Override
    public void requestAsString(Request request, Handler<AsyncResult<String>> resultHandler) {
        execute(request, buffer -> {
            resultHandler.handle(new AsyncResult<String>() {
                @Override
                public String result() {
                    return buffer.toString();
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    @Override
    public void requestAsJson(Request request, Handler<AsyncResult<JsonObject>> resultHandler) {
        execute(request, buffer -> {
            resultHandler.handle(new AsyncResult<JsonObject>() {
                @Override
                public JsonObject result() {
                    return buffer.toJsonObject();
                }

                @Override
                public Throwable cause() {
                    return null;
                }

                @Override
                public boolean succeeded() {
                    return true;
                }

                @Override
                public boolean failed() {
                    return false;
                }
            });
        });
    }

    private void execute(AbstractRequest request, Handler<Buffer> handler) {
        httpClient.getAbs(apiPath + request.toQuery(), r -> {
            r.bodyHandler(handler);
        }).end();
    }
}
