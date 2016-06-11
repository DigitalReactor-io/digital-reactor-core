package io.digitalreactor.core.promise.oncontext;

import io.vertx.core.Context;
import io.vertx.core.Vertx;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public class Promise {

    public static PromiseFactory onContext(Vertx vertx) {
        return onContext(vertx.getOrCreateContext());
    }

    public static PromiseFactory onContext(Context context) {
        return new PromiseFactoryImpl(context);
    }
}
