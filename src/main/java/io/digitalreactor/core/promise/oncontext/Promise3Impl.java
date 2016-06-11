package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.*;
import io.digitalreactor.core.promise.functions.Function3;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public class Promise3Impl<A, B, C> extends Promise0Impl implements Promise3<A, B, C> {

    private final Future<A> futureA;
    private final Future<B> futureB;
    private final Future<C> futureC;

    public Promise3Impl(
            AbstractPromise first,
            AbstractPromiseFactory promiseFactory,
            Context context,
            Future<A> futureA,
            Future<B> futureB,
            Future<C> futureC
    ) {
        super(first, promiseFactory, context, futureA, futureB, futureC);
        this.futureA = futureA;
        this.futureB = futureB;
        this.futureC = futureC;
    }

    @Override
    public <D> Promise1<D> then(Consumer4<A, B, C, Future<D>> consumer) {
        Future<D> futureD = Future.future();
        Promise1<D> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureD);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD);
            });
        });
        return promise1;
    }

    @Override
    public <D, E> Promise2<D, E> then(Consumer5<A, B, C, Future<D>, Future<E>> consumer) {
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Promise2<D, E> promise2 = new Promise2Impl<>(this.first, this.promiseFactory, this.context, futureD, futureE);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD, futureE);
            });
        });
        return promise2;
    }

    @Override
    public <D, E, F> Promise3<D, E, F> then(Consumer6<A, B, C, Future<D>, Future<E>, Future<F>> consumer) {
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Future<F> futureF = Future.future();
        Promise3<D, E, F> promise3 = new Promise3Impl<>(this.first, this.promiseFactory, this.context, futureD, futureE, futureF);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC.result(), futureD, futureE, futureF);
            });
        });
        return promise3;
    }


    @Override
    public void done(PromiseHandler handler) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                handler.apply();
            });
        });
        eval();
    }

    @Override
    public void then(Consumer1<A> consumer) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result());
            });
        });
        eval();
    }

    @Override
    public void then(Consumer2<A, B> consumer) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result());
            });
        });
        eval();
    }

    @Override
    public void then(Consumer3<A, B, C> consumer) {
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC.result());
            });
        });
        eval();
    }

    @Override
    public <D> Promise1<D> thenBlocking(Function3<A, B, C, D> function) {
        Future<D> futureD = Future.future();
        Promise1Impl<D> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureD);
        CompositeFuture.all(futureA, futureB, futureC).setHandler(r -> {
            context.executeBlocking((Future<D> f) -> {
                f.complete(function.apply(futureA.result(), futureB.result(), futureC.result()));
            }, fr -> {
                futureD.complete(fr.result());
            });
        });
        return promise1;
    }


}
