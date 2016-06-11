package io.digitalreactor.core.promise.oncontext;

import io.digitalreactor.core.promise.consumers.Consumer1;
import io.digitalreactor.core.promise.consumers.Consumer2;
import io.digitalreactor.core.promise.consumers.Consumer3;
import io.digitalreactor.core.promise.functions.Function0;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Context;
import io.vertx.core.Future;

/**
 * Created by flaidzeres on 09.06.2016.
 */
public class Promise0Impl extends AbstractPromise implements Promise0 {

    private final Future future1;
    private final Future future2;
    private final Future future3;

    private final int count;

    public Promise0Impl(
            final AbstractPromise first,
            final AbstractPromiseFactory promiseFactory,
            final Context context,
            final Future future1
    ) {
        this(first, promiseFactory, context, future1, null, null, 1);
    }

    public Promise0Impl(
            final AbstractPromise parent,
            final AbstractPromiseFactory promiseFactory,
            final Context context,
            final Future future1,
            final Future future2
    ) {
        this(parent, promiseFactory, context, future1, future2, null, 2);
    }

    public Promise0Impl(
            final AbstractPromise parent,
            final AbstractPromiseFactory promiseFactory,
            final Context context,
            final Future future1,
            final Future future2,
            final Future future3
    ) {
        this(parent, promiseFactory, context, future1, future2, future3, 3);
    }

    public Promise0Impl(
            final AbstractPromise parent,
            final AbstractPromiseFactory promiseFactory,
            final Context context,
            final Future future1,
            final Future future2,
            final Future future3,
            int count
    ) {
        super(parent, promiseFactory, context);
        this.future1 = future1;
        this.future2 = future2;
        this.future3 = future3;
        this.count = count;
    }

    @Override
    public <A> Promise1<A> andThen(Consumer1<Future<A>> consumer) {
        Future<A> future = Future.future();
        Promise1<A> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, future);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(future);
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(future);
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(future);
                    });
                });
                break;
        }
        return promise1;

    }

    @Override
    public <A, B> Promise2<A, B> andThen(Consumer2<Future<A>, Future<B>> consumer) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();

        Promise2<A, B> promise2 = new Promise2Impl<>(this.first, this.promiseFactory, this.context, futureA, futureB);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB);
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB);
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB);
                    });
                });
                break;
        }

        return promise2;
    }

    @Override
    public <A, B, C> Promise3<A, B, C> andThen(Consumer3<Future<A>, Future<B>, Future<C>> consumer) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();

        Promise3<A, B, C> promise3 = new Promise3Impl<>(this.first, this.promiseFactory, this.context, futureA, futureB, futureC);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB, futureC);
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB, futureC);
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.runOnContext(c -> {
                        consumer.apply(futureA, futureB, futureC);
                    });
                });
                break;
        }

        return promise3;
    }

    @Override
    public <A> Promise1<A> andThenBlocking(Function0<A> function) {
        Future<A> future = Future.future();
        Promise1<A> promise1 = new Promise1Impl<>(this.first, this.promiseFactory, this.context, future);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.executeBlocking((Future<A> f) -> {
                        f.complete(function.apply());
                    }, fr -> {
                        future.complete(fr.result());
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.executeBlocking((Future<A> f) -> {
                        f.complete(function.apply());
                    }, fr -> {
                        future.complete(fr.result());
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.executeBlocking((Future<A> f) -> {
                        f.complete(function.apply());
                    }, fr -> {
                        future.complete(fr.result());
                    });
                });
                break;
        }
        return promise1;
    }

    @Override
    public <A, B> Promise2<A, B> andThenBlocking(Function0<A> function1, Function0<B> function2) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Promise2<A, B> promise2 = new Promise2Impl<>(this.first, this.promiseFactory, this.context, futureA, futureB);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
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
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
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
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
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
                });
                break;
        }
        return promise2;
    }

    @Override
    public <A, B, C> Promise3<A, B, C> andThenBlocking(Function0<A> function1, Function0<B> function2, Function0<C> function3) {
        Future<A> futureA = Future.future();
        Future<B> futureB = Future.future();
        Future<C> futureC = Future.future();

        Promise3<A, B, C> promise3 = new Promise3Impl<>(this.first, this.promiseFactory, this.context, futureA, futureB, futureC);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.runOnContext(c -> {
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
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.runOnContext(c -> {
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
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.runOnContext(c -> {
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
                });
                break;
        }

        return promise3;
    }

    @Override
    public Promise0 andThenBlocking(PromiseHandler handler) {
        Future future = Future.future();
        Promise0Impl promise1 = new Promise0Impl(this.first, this.promiseFactory, this.context, future);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler.apply();
                        f.complete();
                    }, fr -> {
                        future.complete();
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler.apply();
                        f.complete();
                    }, fr -> {
                        future.complete();
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler.apply();
                        f.complete();
                    }, fr -> {
                        future.complete();
                    });
                });
                break;
        }
        return promise1;
    }

    @Override
    public Promise0 andThenBlocking(PromiseHandler handler1, PromiseHandler handler2) {
        Future futureA = Future.future();
        Future futureB = Future.future();
        Promise0Impl promise2 = new Promise0Impl(this.first, this.promiseFactory, this.context, futureA, futureB);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                });
                break;
        }
        return promise2;
    }

    @Override
    public Promise0 andThenBlocking(PromiseHandler handler1, PromiseHandler handler2, PromiseHandler handler3) {
        Future futureA = Future.future();
        Future futureB = Future.future();
        Future futureC = Future.future();
        Promise0Impl promise3 = new Promise0Impl(this.first, this.promiseFactory, this.context, futureA, futureB, futureC);
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                    context.executeBlocking(f -> {
                        handler3.apply();
                        f.complete();
                    }, fr -> {
                        futureC.complete();
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                    context.executeBlocking(f -> {
                        handler3.apply();
                        f.complete();
                    }, fr -> {
                        futureC.complete();
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.executeBlocking(f -> {
                        handler1.apply();
                        f.complete();
                    }, fr -> {
                        futureA.complete();
                    });
                    context.executeBlocking(f -> {
                        handler2.apply();
                        f.complete();
                    }, fr -> {
                        futureB.complete();
                    });
                    context.executeBlocking(f -> {
                        handler3.apply();
                        f.complete();
                    }, fr -> {
                        futureC.complete();
                    });
                });
                break;
        }
        return promise3;
    }

    @Override
    public void done(PromiseHandler handler) {
        switch (count) {
            case 1:
                this.future1.setHandler(r -> {
                    context.runOnContext(c -> {
                        handler.apply();
                    });
                });
                break;
            case 2:
                CompositeFuture.all(future1, future2).setHandler(r -> {
                    context.runOnContext(c -> {
                        handler.apply();
                    });
                });
                break;
            case 3:
                CompositeFuture.all(future1, future2, future3).setHandler(r -> {
                    context.runOnContext(c -> {
                        handler.apply();
                    });
                });
                break;
        }
        promiseFactory.eval(first);
    }
}
