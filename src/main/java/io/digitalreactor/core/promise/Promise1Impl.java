package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.func.Consumer1;
import io.digitalreactor.core.promise.func.Consumer2;
import io.digitalreactor.core.promise.func.Consumer3;
import io.digitalreactor.core.promise.func.Consumer4;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public class Promise1Impl<A> extends AbstractPromise implements Promise1<A> {

    private final Future<A> futureA;

    private final Context context;

    private Promise1Impl(
            final Context context,
            final Future<A> futureA,
            final AbstractPromise parent,
            final PromiseHandler handler
    ) {
        super(parent, handler, context);
        this.futureA = futureA;
        this.context = context;
    }

    public static <A> Promise1<A> of(Context context, Future<A> futureA, PromiseHandler handler) {
        return new Promise1Impl<>(context, futureA, null, handler);
    }

    public static <A> Promise1<A> of(Future<A> futureA, AbstractPromise parent, PromiseHandler handler) {
        return new Promise1Impl<>(null, futureA, parent, handler);
    }

    public static <A> Promise1<A> of(Context context, Future<A> futureA, AbstractPromise parent) {
        return new Promise1Impl<>(context, futureA, parent, null);
    }

    public static <A> Promise1<A> of(Context context, Future<A> futureA, AbstractPromise parent, PromiseHandler handler) {
        return new Promise1Impl<>(context, futureA, parent, handler);
    }

    @Override
    public <B> Promise1<B> then(Consumer2<A, Future<B>> consumer) {
        Future<B> futureA = Future.future();
        Promise1<B> promise1 = Promise1Impl.of(context, futureA, this);
        this.futureA.setHandler(r -> {
            consumer.apply(r.result(), futureA);
        });
        return promise1;
    }

    @Override
    public <B, C> Promise2<B, C> then(Consumer3<A, Future<B>, Future<C>> consumer) {
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Promise2<B, C> promise2 = Promise2Impl.of(futureB, futureC, this);
        this.futureA.setHandler(r -> {
            consumer.apply(r.result(), futureB, futureC);
        });
        return promise2;
    }

    @Override
    public <B, C, D> Promise3<B, C, D> then(Consumer4<A, Future<B>, Future<C>, Future<D>> consumer) {
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Promise3<B, C, D> promise3 = Promise3Impl.of(futureB, futureC, futureD, this);
        futureA.setHandler(r -> {
            consumer.apply(r.result(), futureB, futureC, futureD);
        });
        return promise3;
    }

    @Override
    public void eval() {
        super.eval();
    }

    @Override
    public void done(PromiseHandler handler) {
        futureA.setHandler(r -> {
            handler.apply();
        });
        eval();
    }

    @Override
    public void then(Consumer1<A> consumer) {
        futureA.setHandler(r -> {
            consumer.apply(r.result());
        });
        eval();
    }

}
