package io.digitalreactor.core.promise.oncontext;

import io.vertx.core.Context;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public abstract class AbstractPromise implements Promise0 {
    protected AbstractPromise first;
    protected AbstractPromiseFactory promiseFactory;
    protected Context context;

    public AbstractPromise(
            final AbstractPromise first,
            final AbstractPromiseFactory promiseFactory,
            final Context context
    ) {
        this.first = first;
        this.promiseFactory = promiseFactory;
        this.context = context;
    }

    @Override
    public void eval() {
        promiseFactory.eval(first);
    }

}
