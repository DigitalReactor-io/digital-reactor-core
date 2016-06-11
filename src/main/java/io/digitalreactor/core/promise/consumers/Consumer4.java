package io.digitalreactor.core.promise.consumers;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@FunctionalInterface
public interface Consumer4<A, B, C, D> {
    void apply(A a, B b, C c, D d);
}
