package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * Created by flaidzeres on 11.05.16.
 */
public abstract class ReactorAbstractVerticle extends AbstractVerticle {

    private final ObjectMapper mapper = JsonFactory.create();

    protected <T> T toObj(String json, Class<T> tClass) {
        return mapper.fromJson(json, tClass);
    }

    protected <T> String fromObj(T t) {
        return mapper.toJson(t);
    }
}
