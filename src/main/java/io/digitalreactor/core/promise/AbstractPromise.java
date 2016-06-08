package io.digitalreactor.core.promise;

import io.vertx.core.Context;

/**
 * Created by dgovorukhin on 07.06.2016.
 */
abstract class AbstractPromise {
    protected final AbstractPromise parent;
    protected final PromiseHandler handler;
    protected final Context context;

    protected AbstractPromise(
            final AbstractPromise parent,
            final PromiseHandler handler,
            final Context context
    ) {
        this.parent = parent;
        this.handler = handler;
        this.context = context;
    }

    protected void handleParent() {
        if (parent != null) {
            parent.eval();
        }
    }

    protected void eval() {
        if (handler != null) {
            handler.apply();
        }
        handleParent();
    }
}
