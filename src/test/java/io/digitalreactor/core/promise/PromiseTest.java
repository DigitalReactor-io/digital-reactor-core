package io.digitalreactor.core.promise;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by flaidzeres on 06.06.2016.
 */
@RunWith(VertxUnitRunner.class)
public class PromiseTest extends AbstractPromiseTest {

    private final static Logger logger = LoggerFactory.getLogger(PromiseTest.class);

    @Test
    public void test1(TestContext context) {
        Vertx vertx = rule.vertx();
        Async async = context.async(1);

        MyVerticle v = new MyVerticle();

        Promise.when((Future<String> f1) -> {
            vertx.deployVerticle(v, r -> {
                f1.complete();
                async.countDown();
            });
        }).eval();

        vertx.deployVerticle(v, r -> {
            async.complete();
        });

    }

    @Test
    public void test2(TestContext context) {
        Vertx vertx = rule.vertx();
        Async async = context.async(1);

        MyVerticle v = new MyVerticle();

        Promise.when((Future<String> f1) -> {
            vertx.deployVerticle(v, r -> {
                f1.complete(r.result());
                async.countDown();
            });

        }).done(() -> {
            async.complete();
        });

    }

    @Test
    public void test3(TestContext context) {
        Vertx vertx = rule.vertx();
        Async async = context.async(1);

        MyVerticle v = new MyVerticle();

        Promise.when((Future<String> f1) -> {
            vertx.deployVerticle(v, r -> {
                f1.complete(r.result());
                async.countDown();
            });

        }).then((id) -> {
            context.assertNotNull(id);
            async.complete();
        });

    }

    @Test
    public void test4(TestContext context) {
        Vertx vertx = rule.vertx();
        Async async = context.async(2);

        MyVerticle v1 = new MyVerticle();
        MyVerticle v2 = new MyVerticle();

        Promise.when((Future<String> f1) -> {
            vertx.deployVerticle(v1, r -> {
                f1.complete(r.result());
                async.countDown();
            });
        }).then((String id1, Future<String> f2) -> {
            context.assertNotNull(id1);
            vertx.deployVerticle(v2, r -> {
                f2.complete(r.result());
                async.countDown();
            });
        }).then(id2 -> {
            context.assertNotNull(id2);
            async.complete();
        });

    }

    @Test
    public void test5(TestContext testContext) {
        Vertx vertx = rule.vertx();
        Context context = vertx.getOrCreateContext();
        Async async = testContext.async(2);

        MyVerticle v1 = new MyVerticle();
        MyVerticle v2 = new MyVerticle();

        Promise.from(context).onContext((Future<String> f1) -> {
            vertx.deployVerticle(v1, r -> {
                f1.complete(r.result());
                async.countDown();
            });
        }).then((String id1, Future<String> f2) -> {
            testContext.assertNotNull(id1);
            vertx.deployVerticle(v2, r -> {
                f2.complete(r.result());
                async.countDown();
            });
        }).then(id2 -> {
            testContext.assertNotNull(id2);
            async.complete();
        });

    }


}
