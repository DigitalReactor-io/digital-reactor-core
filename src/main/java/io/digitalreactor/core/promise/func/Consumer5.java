package io.digitalreactor.core.promise.func;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@FunctionalInterface
public interface Consumer5<A, B, C, D, E> {
    void apply(A a, B b, C c, D d, E e);
}
