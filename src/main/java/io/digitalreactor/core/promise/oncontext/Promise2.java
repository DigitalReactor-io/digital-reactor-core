package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.*;
import io.digitalreactor.core.promise.functions.Function2;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Promise2<A, B>  extends Promise0 {

    <C> Promise1<C> then(Consumer3<A, B, Future<C>> consumer);

    <C, D> Promise2<C, D> then(Consumer4<A, B, Future<C>, Future<D>> consumer);

    <C, D, E> Promise3<C, D, E> then(Consumer5<A, B, Future<C>, Future<D>, Future<E>> consumer);

    void done(PromiseHandler handler);

    void then(Consumer1<A> consumer);

    void then(Consumer2<A, B> consumer);

    <C> Promise1<C> thenBlocking(Function2<A, B, C> function);
}
