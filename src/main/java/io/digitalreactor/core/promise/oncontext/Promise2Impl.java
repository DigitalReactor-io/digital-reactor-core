package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.*;
import io.digitalreactor.core.promise.functions.Function2;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public class Promise2Impl<A, B> extends Promise0Impl implements Promise2<A, B> {
    private final Future<A> futureA;
    private final Future<B> futureB;

    public Promise2Impl(
            AbstractPromise first,
            AbstractPromiseFactory promiseFactory,
            Context context,
            Future<A> futureA,
            Future<B> futureB
    ) {
        super(first, promiseFactory, context, futureA, futureB);
        this.futureA = futureA;
        this.futureB = futureB;
    }

    @Override
    public <C> Promise1<C> then(Consumer3<A, B, Future<C>> consumer) {
        Future<C> futureC = Future.future();
        Promise1<C> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureC);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC);
            });
        });
        return promise1;
    }

    @Override
    public <C, D> Promise2<C, D> then(Consumer4<A, B, Future<C>, Future<D>> consumer) {
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Promise2<C, D> promise2 = new Promise2Impl<>(this.first, this.promiseFactory, this.context, futureC, futureD);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC, futureD);
            });
        });
        return promise2;
    }

    @Override
    public <C, D, E> Promise3<C, D, E> then(Consumer5<A, B, Future<C>, Future<D>, Future<E>> consumer) {
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Future<E> futureE = Future.future();
        Promise3<C, D, E> promise3 = new Promise3Impl<>(this.first, this.promiseFactory, this.context, futureC, futureD, futureE);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result(), futureC, futureD, futureE);
            });
        });
        return promise3;
    }


    @Override
    public void done(PromiseHandler handler) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                handler.apply();
            });
        });
        eval();
    }

    @Override
    public void then(Consumer1<A> consumer) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result());
            });
        });
        eval();
    }

    @Override
    public void then(Consumer2<A, B> consumer) {
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(futureA.result(), futureB.result());
            });
        });
        eval();
    }

    @Override
    public <C> Promise1<C> thenBlocking(Function2<A, B, C> function) {
        Future<C> futureC = Future.future();
        Promise1<C> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureC);
        CompositeFuture.all(futureA, futureB).setHandler(r -> {
            context.executeBlocking((Future<C> f) -> {
                f.complete(function.apply(futureA.result(), futureB.result()));
            }, fr -> {
                futureC.complete(fr.result());
            });
        });
        return promise1;
    }


}
