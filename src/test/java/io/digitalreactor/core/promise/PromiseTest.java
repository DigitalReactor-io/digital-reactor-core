package io.digitalreactor.core.promise;

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
public class PromiseTest extends AbstractPromiseTest {

    @Test
    public void test_async_one(TestContext context) {
        Vertx vertx = rule.vertx();
        Async async = context.async();

        MyVerticle v = new MyVerticle();

        Promise.when((Future<String> f1) -> {
            vertx.deployVerticle(v, f1.completer());
        }).then((id) -> {
            context.assertNotNull(id);
            async.complete();
        });

    }


}
