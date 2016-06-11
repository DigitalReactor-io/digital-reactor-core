package io.digitalreactor.core.promise.consumers;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@FunctionalInterface
public interface Consumer6<A, B, C, D, E, F> {
    void apply(A a, B b, C c, D d, E e, F f);
}
