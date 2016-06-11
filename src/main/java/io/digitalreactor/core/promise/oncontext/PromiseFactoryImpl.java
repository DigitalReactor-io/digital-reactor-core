package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.Consumer1;
import io.digitalreactor.core.promise.consumers.Consumer2;
import io.digitalreactor.core.promise.consumers.Consumer3;
import io.digitalreactor.core.promise.functions.Function0;
import io.vertx.core.Context;
import io.vertx.core.Future;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dgovorukhin on 09.06.2016.
 */
public class PromiseFactoryImpl extends AbstractPromiseFactory implements PromiseFactory {

    private final Context context;

    private Map<Promise0, PromiseHandler> handlers = new HashMap<>();

    public PromiseFactoryImpl() {
        this(null);
    }

    public PromiseFactoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public <A> Promise1<A> when(Consumer1<Future<A>> consumer) {
        Future<A> future = Future.future();
        Promise1Impl<A> promise1 = new Promise1Impl<>(null, this, context, future);
        promise1.first = promise1;
        this.handlers.put(
                promise1,
                () -> {
                    if (context != null) {
                        context.runOnContext((o) -> {
                            consumer.apply(future);
                        });
                    } else {
                        consumer.apply(future);
                    }
                }
        );
        return promise1;
    }

    @Override
    public <A, B> Promise2<A, B> when(Consumer2<Future<A>, Future<B>> consumer) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Promise2Impl<A, B> promise2 = new Promise2Impl<>(null, this, context, futureA, futureB);
        promise2.first = promise2;
        this.handlers.put(
                promise2,
                () -> {
                    if (context != null) {
                        context.runOnContext((o) -> {
                            consumer.apply(futureA, futureB);
                        });
                    } else {
                        consumer.apply(futureA, futureB);
                    }
                }
        );
        return promise2;
    }

    @Override
    public <A, B, C> Promise3<A, B, C> when(Consumer3<Future<A>, Future<B>, Future<C>> consumer) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Promise3Impl<A, B, C> promise3 = new Promise3Impl<>(null, this, context, futureA, futureB, futureC);
        promise3.first = promise3;
        this.handlers.put(
                promise3,
                () -> {
                    if (context != null) {
                        context.runOnContext((o) -> {
                            consumer.apply(futureA, futureB, futureC);
                        });
                    } else {
                        consumer.apply(futureA, futureB, futureC);
                    }
                }
        );
        return promise3;
    }

    @Override
    public <A> Promise1<A> whenBlocking(Function0<A> function) {
        Future<A> future = Future.future();
        Promise1Impl<A> promise1 = new Promise1Impl<>(null, this, context, future);
        promise1.first = promise1;
        this.handlers.put(
                promise1,
                () -> {
                    if (context != null) {
                        context.executeBlocking((Future<A> f) -> {
                            f.complete(function.apply());
                        }, fr -> {
                            future.complete(fr.result());
                        });
                    }
                }
        );
        return promise1;
    }

    @Override
    public <A, B> Promise2<A, B> whenBlocking(Function0<A> function1, Function0<B> function2) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Promise2<A, B> promise2 = new Promise2Impl<>(null, this, context, futureA, futureB);
        this.handlers.put(
                promise2,
                () -> {
                    if (context != null) {
                        context.executeBlocking((Future<A> f) -> {
                            f.complete(function1.apply());
                        }, fr -> {
                            futureA.complete(fr.result());
                        });
                        context.executeBlocking((Future<B> f) -> {
                            f.complete(function2.apply());
                        }, fr -> {
                            futureB.complete(fr.result());
                        });
                    }
                }
        );
        return promise2;
    }

    @Override
    public <A, B, C> Promise3<A, B, C> whenBlocking(Function0<A> function1, Function0<B> function2, Function0<C> function3) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();
        Promise3Impl<A, B, C> promise3 = new Promise3Impl<>(null, this, context, futureA, futureB, futureC);
        promise3.first = promise3;
        this.handlers.put(
                promise3,
                () -> {
                    context.runOnContext((o) -> {
                        context.executeBlocking((Future<A> f) -> {
                            f.complete(function1.apply());
                        }, fr -> {
                            futureA.complete(fr.result());
                        });
                        context.executeBlocking((Future<B> f) -> {
                            f.complete(function2.apply());
                        }, fr -> {
                            futureB.complete(fr.result());
                        });
                        context.executeBlocking((Future<C> f) -> {
                            f.complete(function3.apply());
                        }, fr -> {
                            futureC.complete(fr.result());
                        });
                    });
                }
        );
        return promise3;
    }

    @Override
    public Promise0 whenBlocking(PromiseHandler handler) {
        Future<Void> future = Future.future();
        Promise0Impl promiseOnContext = new Promise0Impl(null, this, context, future);
        promiseOnContext.first = promiseOnContext;
        this.handlers.put(
                promiseOnContext,
                () -> {
                    context.executeBlocking(f -> {
                        handler.apply();
                        f.complete();
                    }, fr -> {
                        future.complete();
                    });
                }
        );
        return promiseOnContext;
    }

    @Override
    public Promise0 whenBlocking(PromiseHandler handler1, PromiseHandler handler2) {
        Future<Void> futureA = Future.future();
        Future<Void> futureB = Future.future();
        Promise0Impl promise = new Promise0Impl(null, this, context, futureA, futureB);
        promise.first = promise;
        this.handlers.put(
                promise,
                () -> {
                    if (context != null) {
                        context.executeBlocking((Future<Void> f) -> {
                            handler1.apply();
                            f.complete();
                        }, fr -> {
                            futureA.complete();
                        });
                        context.executeBlocking((Future<Void> f) -> {
                            handler2.apply();
                            f.complete();
                        }, fr -> {
                            futureB.complete();
                        });
                    }
                }
        );
        return promise;
    }

    @Override
    public Promise0 whenBlocking(PromiseHandler handler1, PromiseHandler handler2, PromiseHandler handler3) {
        Future<Void> futureA = Future.future();
        Future<Void> futureB = Future.future();
        Future<Void> futureC = Future.future();
        Promise0Impl promise = new Promise0Impl(null, this, context, futureA, futureB, futureC);
        promise.first = promise;
        this.handlers.put(
                promise,
                () -> {
                    if (context != null) {
                        context.executeBlocking((Future<Void> f) -> {
                            handler1.apply();
                            f.complete();
                        }, fr -> {
                            futureA.complete();
                        });
                        context.executeBlocking((Future<Void> f) -> {
                            handler2.apply();
                            f.complete();
                        }, fr -> {
                            futureB.complete();
                        });
                        context.executeBlocking((Future<Void> f) -> {
                            handler3.apply();
                            f.complete();
                        }, fr -> {
                            futureC.complete();
                        });
                    }
                }
        );
        return promise;
    }

    @Override
    void eval(Promise0 promise0) {
        handlers.get(promise0).apply();
    }


}
