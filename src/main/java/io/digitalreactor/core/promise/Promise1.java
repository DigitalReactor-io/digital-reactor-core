package io.digitalreactor.core.promise;

import io.digitalreactor.core.promise.func.Consumer1;
import io.digitalreactor.core.promise.func.Consumer2;
import io.digitalreactor.core.promise.func.Consumer3;
import io.digitalreactor.core.promise.func.Consumer4;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 06.06.2016.
 */
public interface Promise1<A> {

    <B> Promise1<B> then(Consumer2<A, Future<B>> consumer);

    <B, C> Promise2<B, C> then(Consumer3<A, Future<B>, Future<C>> consumer);

    <B, C, D> Promise3<B, C, D> then(Consumer4<A, Future<B>, Future<C>, Future<D>> consumer);

    void eval();

    void done(PromiseHandler handler);

    void then(Consumer1<A> consumer);

}
