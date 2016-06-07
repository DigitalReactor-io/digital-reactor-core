package io.digitalreactor.core.promise;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.unit.junit.RunTestOnContext;
import org.junit.Rule;

/**
 * Created by flaidzeres on 07.06.2016.
 */
public abstract class AbstractPromiseTest {

    @Rule
    public RunTestOnContext rule = new RunTestOnContext();


    protected static class MyVerticle extends AbstractVerticle {

    }

    protected static String threadName() {
        return Thread.currentThread().getName();
    }
}
