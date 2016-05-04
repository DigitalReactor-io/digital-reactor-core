package io.digitalreactor.core.application;

import io.digitalreactor.core.UserManagerVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * Created by ingvard on 03.05.16.
 */
public class UserManagerAuthProvider implements AuthProvider {

    private Vertx vertx;

    public UserManagerAuthProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
        vertx.eventBus().send(UserManagerVerticle.AUTHENTICATE, authInfo, reply -> {
            if (reply.succeeded()) {
                resultHandler.handle(Future.succeededFuture(new AbstractUser() {
                    @Override
                    public JsonObject principal() {
                        return null;
                    }

                    @Override
                    public void setAuthProvider(AuthProvider authProvider) {

                    }

                    @Override
                    protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> resultHandler) {

                    }
                }));
            } else {
                resultHandler.handle(Future.failedFuture(reply.cause().getMessage()));
            }
        });
    }
}
