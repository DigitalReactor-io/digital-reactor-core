package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.func.Consumer1;
import io.digitalreactor.core.promise.func.Consumer2;
import io.digitalreactor.core.promise.func.Consumer3;
import io.vertx.core.Future;

/**
 * Created by dgovorukhin on 07.06.2016.
 */
public class Promise {

    public static <B> Promise1<B> when(Consumer1<Future<B>> consumer) {
        Future<B> f = Future.future();
        return Promise1Impl.of(f, null, () -> {
            consumer.apply(f);
        });
    }

    public static <A, B> Promise2<A, B> when(Consumer2<Future<A>, Future<B>> consumer) {
        Future<A> f1 = Future.future();
        Future<B> f2 = Future.future();
        return Promise2Impl.of(f1, f2, null, () -> {
            consumer.apply(f1, f2);
        });
    }

    public static <A, B, C> Promise3<A, B, C> when(Consumer3<Future<A>, Future<B>, Future<C>> consumer) {
        Future<A> f1 = Future.future();
        Future<B> f2 = Future.future();
        Future<C> f3 = Future.future();
        return Promise3Impl.of(f1, f2, f3, null, () -> {
            consumer.apply(f1, f2, f3);
        });
    }

}
