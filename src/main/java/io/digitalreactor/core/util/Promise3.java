package io.digitalreactor.core.util;

import io.digitalreactor.core.util.func.*;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public interface Promise3<A, B, C> {

    <D> Promise1<D> then(Consumer4<A, B, C, Future<D>> consumer);

    <D, E> Promise2<D, E> then(Consumer5<A, B, C, Future<D>, Future<E>> consumer);

    <D, E, F> Promise3<D, E, F> then(Consumer6<A, B, C, Future<D>, Future<E>, Future<F>> consumer);

    void eval();

    void done(PromiseHandler handler);

    void then(Consumer1<A> consumer);

    void then(Consumer2<A, B> consumer);

    void then(Consumer3<A, B, C> consumer);

}
