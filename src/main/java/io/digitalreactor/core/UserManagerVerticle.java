package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * Created by ingvard on 02.05.16.
 */
public class UserManagerVerticle extends AbstractVerticle {

    private static String BASE_USER_MANAGER = "digitalreactor.core.user.";
    public static String NEW_USER = BASE_USER_MANAGER + "new";

    private EventBus eventBus;

    @Override
    public void start() throws Exception {
        eventBus = vertx.eventBus();

        eventBus.consumer(NEW_USER, this::newUser);
    }

    private void newUser(Message newUserMessage) {
        newUserMessage.reply("Success");
    }

}
