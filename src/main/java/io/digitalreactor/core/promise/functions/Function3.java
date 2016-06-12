package io.digitalreactor.core.promise.functions;

/**
 * Created by flaidzeres on 08.06.2016.
 */
public interface Function3<A, B, C, D> {
    D apply(A a, B b, C c);
}
