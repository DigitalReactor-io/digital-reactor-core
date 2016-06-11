package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.Consumer1;
import io.digitalreactor.core.promise.consumers.Consumer2;
import io.digitalreactor.core.promise.consumers.Consumer3;
import io.digitalreactor.core.promise.consumers.Consumer4;
import io.digitalreactor.core.promise.functions.Function1;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public class Promise1Impl<A> extends Promise0Impl implements Promise1<A> {

    private final Future<A> futureA;

    public Promise1Impl(
            AbstractPromise first,
            AbstractPromiseFactory promiseFactory,
            Context context,
            Future<A> futureA
    ) {
        super(first, promiseFactory, context, futureA);
        this.futureA = futureA;
    }

    @Override
    public <B> Promise1<B> then(Consumer2<A, Future<B>> consumer) {
        Future<B> futureB = Future.future();
        Promise1<B> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureB);
        this.futureA.setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(r.result(), futureB);
            });
        });
        return promise1;
    }

    @Override
    public <B, C> Promise2<B, C> then(Consumer3<A, Future<B>, Future<C>> consumer) {
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Promise2<B, C> promise2 = new Promise2Impl<>(this.first, this.promiseFactory, this.context, futureB, futureC);
        this.futureA.setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(r.result(), futureB, futureC);
            });
        });
        return promise2;
    }

    @Override
    public <B, C, D> Promise3<B, C, D> then(Consumer4<A, Future<B>, Future<C>, Future<D>> consumer) {
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Future<D> futureD = Future.future();
        Promise3<B, C, D> promise3 = new Promise3Impl<>(this.first, this.promiseFactory, this.context, futureB, futureC, futureD);
        futureA.setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(r.result(), futureB, futureC, futureD);
            });
        });
        return promise3;
    }

    @Override
    public void done(PromiseHandler handler) {
        futureA.setHandler(r -> {
            context.runOnContext(c -> {
                handler.apply();
            });
        });
        eval();
    }

    @Override
    public void then(Consumer1<A> consumer) {
        futureA.setHandler(r -> {
            context.runOnContext(c -> {
                consumer.apply(r.result());
            });
        });
        eval();
    }

    @Override
    public <B> Promise1<B> thenBlocking(Function1<A, B> function) {
        Future<B> futureB = Future.future();
        Promise1Impl<B> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, futureB);
        this.futureA.setHandler(r -> {
            context.executeBlocking((Future<B> f) -> {
                f.complete(function.apply(r.result()));
            }, fr -> {
                futureB.complete(fr.result());
            });
        });
        return promise1;
    }

}
