package io.digitalreactor.core.util;

/**
 * Created by dgovorukhin on 07.06.2016.
 */
abstract class AbstractPromise {
    protected final AbstractPromise parent;
    protected final PromiseHandler handler;

    protected AbstractPromise(
            final AbstractPromise parent,
            final PromiseHandler handler
    ) {
        this.parent = parent;
        this.handler = handler;
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
