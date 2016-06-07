package io.digitalreactor.core.util.func;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@FunctionalInterface
public interface Consumer2<A, B> {
    void apply(A a, B b);
}
