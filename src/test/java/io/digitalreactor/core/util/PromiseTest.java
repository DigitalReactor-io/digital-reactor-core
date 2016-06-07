package io.digitalreactor.core.util;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@RunWith(VertxUnitRunner.class)
public class PromiseTest {


    @Test
    public void test(TestContext context) {
        Async async = context.async();

        Vertx vertx = Vertx.vertx();
        MyVerticle v1 = new MyVerticle();
        MyVerticle v2 = new MyVerticle();
        MyVerticle v3 = new MyVerticle();

        System.out.println("before1 " + threadName());

        Promise.when((Future<String> f1, Future<String> f2, Future<String> f3) -> {
            vertx.deployVerticle(v1, r -> {
                System.out.println("deploy v1 " + threadName());
                f1.complete(r.result());
            });
            vertx.deployVerticle(v2, r -> {
                System.out.println("deploy v2 " + threadName());
                f2.complete(r.result());
            });
            vertx.deployVerticle(v3, r -> {
                System.out.println("deploy v3 " + threadName());
                f3.complete(r.result());
            });
        }).then((String r1, String r2, String r3, Future<Void> f1, Future<Void> f2, Future<Void> f3) -> {
            System.out.println("all deploy 1 " + threadName());
            vertx.undeploy(r1, f1.completer());
            vertx.undeploy(r2, f2.completer());
            vertx.undeploy(r3, f3.completer());
        }).done(() -> {
            System.out.println("all unDeploy 1 " + threadName());
            async.complete();
        });

        System.out.println("after1 " + threadName());

    }

    class MyVerticle extends AbstractVerticle {

    }

    public static String threadName() {
        return Thread.currentThread().getName();
    }

}
