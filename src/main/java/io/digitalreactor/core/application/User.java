package io.digitalreactor.core.application;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AbstractUser;
import io.vertx.ext.auth.AuthProvider;

/**
 * Created by ingvard on 10.05.16.
 */
public class User extends AbstractUser {

    private final int id;
    private final String email;

    public User(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public int id() {
        return id;
    }

    public String email() {
        return email;
    }

    @Override
    protected void doIsPermitted(String permission, Handler<AsyncResult<Boolean>> resultHandler) {

    }

    @Override
    public JsonObject principal() {
        return null;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {

    }
}
