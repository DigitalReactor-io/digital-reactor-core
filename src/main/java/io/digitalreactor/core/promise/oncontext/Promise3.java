package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.*;
import io.digitalreactor.core.promise.functions.Function3;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Promise3<A, B, C> extends Promise0 {

    <D> Promise1<D> then(Consumer4<A, B, C, Future<D>> consumer);

    <D, E> Promise2<D, E> then(Consumer5<A, B, C, Future<D>, Future<E>> consumer);

    <D, E, F> Promise3<D, E, F> then(Consumer6<A, B, C, Future<D>, Future<E>, Future<F>> consumer);

    void done(PromiseHandler handler);

    void then(Consumer1<A> consumer);

    void then(Consumer2<A, B> consumer);

    void then(Consumer3<A, B, C> consumer);

    <D> Promise1<D> thenBlocking(Function3<A, B, C, D> function);
}
