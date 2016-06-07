package io.digitalreactor.core.util;

import io.digitalreactor.core.util.func.*;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public class Promise2Impl<A, B> extends AbstractPromise implements Promise2<A, B> {

    private final Future<A> futureA;
    private final Future<B> futureB;

    private Promise2Impl(
            Future<A> futureA,
            Future<B> futureB,
            AbstractPromise parent,
            PromiseHandler handler
    ) {
        super(parent, handler);
        this.futureA = futureA;
        this.futureB = futureB;
    }

    public static <A, B> Promise2<A, B> of(Future<A> futureA, Future<B> futureB, AbstractPromise parent) {
        return new Promise2Impl<>(futureA, futureB, parent, null);
    }


    public static <A, B> Promise2<A, B> of(Future<A> futureA, Future<B> futureB, AbstractPromise parent, PromiseHandler handler) {
        return new Promise2Impl<>(futureA, futureB, parent, handler);
    }

    @Override
    public <C> Promise1<C> then(Consumer3<A, B, Future<C>> consumer) {
        Future<C> futureC = Future.future();
        Promise1<C> promise1 = Promise1Impl.of(futureC, this);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC);
        });
        return promise1;
    }

    @Override
    public <C, D> Promise2<C, D> then(Consumer4<A, B, Future<C>, Future<D>> consumer) {
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Promise2<C, D> promise2 = Promise2Impl.of(futureC, futureD, this);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC, futureD);
        });
        return promise2;
    }

    @Override
    public <C, D, E> Promise3<C, D, E> then(Consumer5<A, B, Future<C>, Future<D>, Future<E>> consumer) {
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Promise3<C, D, E> promise3 = Promise3Impl.of(futureC, futureD, futureE, this);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result(), futureC, futureD, futureE);
        });
        return promise3;
    }

    @Override
    public void eval() {
        super.eval();
    }

    @Override
    public void done(PromiseHandler handler) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
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

    @Override
    public void then(Consumer2<A, B> consumer) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            consumer.apply(futureA.result(), futureB.result());
        });
        eval();
    }
}
