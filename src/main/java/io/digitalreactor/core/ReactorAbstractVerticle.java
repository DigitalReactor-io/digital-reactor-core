package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * Created by flaidzeres on 11.05.16.
 */
public abstract class ReactorAbstractVerticle extends AbstractVerticle {

    private static final ObjectMapper mapper = JsonFactory.create();

    protected static  <T> T toObj(Message message, Class<T> tClass) {
        return mapper.fromJson((String) message.body(), tClass);
    }

    protected static <T> T toObj(String json, Class<T> tClass) {
        return mapper.fromJson(json, tClass);
    }

    protected static <T> String toJson(T t) {
        return mapper.toJson(t);
    }
}
