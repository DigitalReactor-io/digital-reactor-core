package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.func.*;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public interface Promise2<A, B> {

    <C> Promise1<C> then(Consumer3<A, B, Future<C>> consumer);

    <C, D> Promise2<C, D> then(Consumer4<A, B, Future<C>, Future<D>> consumer);

    <C, D, E> Promise3<C, D, E> then(Consumer5<A, B, Future<C>, Future<D>, Future<E>> consumer);

    void eval();

    void done(PromiseHandler handler);

    void then(Consumer1<A> consumer);

    void then(Consumer2<A, B> consumer);

}
