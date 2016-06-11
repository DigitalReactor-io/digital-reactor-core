package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.Consumer1;
import io.digitalreactor.core.promise.consumers.Consumer2;
import io.digitalreactor.core.promise.consumers.Consumer3;
import io.digitalreactor.core.promise.consumers.Consumer4;
import io.digitalreactor.core.promise.functions.Function1;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Promise1<A> extends Promise0 {

    <B> Promise1<B> then(Consumer2<A, Future<B>> consumer);

    <B, C> Promise2<B, C> then(Consumer3<A, Future<B>, Future<C>> consumer);

    <B, C, D> Promise3<B, C, D> then(Consumer4<A, Future<B>, Future<C>, Future<D>> consumer);

    void then(Consumer1<A> consumer);

    <B> Promise1<B> thenBlocking(Function1<A, B> function);


}
