package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.func.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public class Promise3Impl<A, B, C> extends AbstractPromise implements Promise3<A, B, C> {

    private final Future<A> futureA;
    private final Future<B> futureB;
    private final Future<C> futureC;

    private Promise3Impl(
            Context context,
            Future<A> futureA,
            Future<B> futureB,
            Future<C> futureC,
            AbstractPromise parent,
            PromiseHandler handler
    ) {
        super(parent, handler, context);
        this.futureA = futureA;
        this.futureB = futureB;
        this.futureC = futureC;
    }

    public static <A, B, C> Promise3<A, B, C> of(
            Future<A> futureA,
            Future<B> futureB,
            Future<C> futureC,
            AbstractPromise parent
    ) {
        return of(null, futureA, futureB, futureC, parent, null);
    }

    public static <A, B, C> Promise3<A, B, C> of(
            Context context,
            Future<A> futureA,
            Future<B> futureB,
            Future<C> futureC,
            AbstractPromise parent,
            PromiseHandler handler
    ) {
        return new Promise3Impl<>(context, futureA, futureB, futureC, parent, handler);
    }

    @Override
    public <D> Promise1<D> then(Consumer4<A, B, C, Future<D>> consumer) {
        Future<D> futureD = Future.future();
        Promise1<D> promise1 = Promise1Impl.of(futureD, this, null);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD);
        });
        return promise1;
    }

    @Override
    public <D, E> Promise2<D, E> then(Consumer5<A, B, C, Future<D>, Future<E>> consumer) {
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Promise2<D, E> promise2 = Promise2Impl.of(futureD, futureE, this);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD, futureE);
        });
        return promise2;
    }

    @Override
    public <D, E, F> Promise3<D, E, F> then(Consumer6<A, B, C, Future<D>, Future<E>, Future<F>> consumer) {
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Future<F> futureF = Future.future();
        Promise3<D, E, F> promise2 = Promise3Impl.of(futureD, futureE, futureF, this);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD, futureE, futureF);
        });
        return promise2;
    }

    @Override
    public void done(PromiseHandler handler) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            handler.apply();
        });
        eval();
    }

    @Override
    public void eval() {
        super.eval();
    }

    @Override
    public void then(Consumer1<A> consumer) {
        futureA.setHandler(r -> {
            consumer.apply(r.result());
        });
        eval();
    }

    @Override
    public void then(Consumer2<A, B> consumer) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result());
        });
        eval();
    }

    @Override
    public void then(Consumer3<A, B, C> consumer) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC.result());
        });
        eval();
    }


}
