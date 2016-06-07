package io.digitalreactor.core.promise.func;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@FunctionalInterface
public interface Consumer3<A,B,C> {
    void apply(A a, B b, C c);
}
