package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.Consumer1;
import io.digitalreactor.core.promise.consumers.Consumer2;
import io.digitalreactor.core.promise.consumers.Consumer3;
import io.digitalreactor.core.promise.functions.Function0;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Promise0 {

    <B> Promise1<B> andThen(Consumer1<Future<B>> consumer);

    <A, B> Promise2<A, B> andThen(Consumer2<Future<A>, Future<B>> consumer);

    <A, B, C> Promise3<A, B, C> andThen(Consumer3<Future<A>, Future<B>, Future<C>> consumer);

    <A> Promise1<A> andThenBlocking(Function0<A> function);

    <A, B> Promise2<A, B> andThenBlocking(Function0<A> function1, Function0<B> function2);

    <A, B, C> Promise3<A, B, C> andThenBlocking(Function0<A> function1, Function0<B> function2, Function0<C> function3);

    Promise0 andThenBlocking(PromiseHandler handler);

    Promise0 andThenBlocking(PromiseHandler handler1, PromiseHandler handler2);

    Promise0 andThenBlocking(PromiseHandler handler1, PromiseHandler handler2, PromiseHandler handler3);

    void eval();

    void done(PromiseHandler handler);
}
