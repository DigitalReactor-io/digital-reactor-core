package io.digitalreactor.core.application;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;

/**
 * Created by ingvard on 03.05.16.
 */
public class UserManagerAuthProvider implements AuthProvider {

    private final EventBus eventBus;
    private final String remoteAuthenticateAddress;

    public UserManagerAuthProvider(EventBus eventBus, String remoteAuthenticateAddress) {
        this.eventBus = eventBus;
        this.remoteAuthenticateAddress = remoteAuthenticateAddress;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
        eventBus.send(remoteAuthenticateAddress, authInfo, reply -> {
            if (reply.succeeded()) {
                JsonObject userInfo = (JsonObject) reply.result().body();
                resultHandler.handle(Future.succeededFuture(
                        new io.digitalreactor.core.application.User(
                                userInfo.getInteger("id"),
                                userInfo.getString("email")
                        ))
                );
            } else {
                resultHandler.handle(Future.failedFuture(reply.cause().getMessage()));
            }
        });
    }
}
