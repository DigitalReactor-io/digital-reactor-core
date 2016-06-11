package io.digitalreactor.core.promise.functions;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Function2<A, B, C> {
    C apply(A a, B b);
}
