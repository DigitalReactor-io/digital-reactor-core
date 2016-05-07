package io.digitalreactor.core.application

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AbstractUser
import spock.lang.Specification
/**
 * Created by ingvard on 04.05.16.
 */
class UserManagerAuthProviderSpec extends Specification {
    private final static String DEFAULT_ADDRESS = "some.address";

    def "Authentication is success, obtains a user"() {
        setup:
        def eventBus = Mock(EventBus);
        def authProvider = new UserManagerAuthProvider(eventBus, DEFAULT_ADDRESS);
        def authInfo = new JsonObject();
        when:
        authProvider.authenticate(authInfo, { res ->
            assert res.succeeded();
            assert res.result() instanceof AbstractUser;
        });
        then:
        1 * eventBus.send(DEFAULT_ADDRESS, authInfo, _ as Handler<AsyncResult<Message>>) >> { address, info, callback ->
            callback.handle(succeeded())
        }
    }

    def "Authentication is fail, obtains a fail reason"() {
        setup:
        def eventBus = Mock(EventBus);
        def authProvider = new UserManagerAuthProvider(eventBus, DEFAULT_ADDRESS);
        def authInfo = new JsonObject();
        def causeMessage = "some problem";
        when:
        authProvider.authenticate(authInfo, { res ->
            assert !res.succeeded();
            assert res.cause().message == causeMessage;
        });
        then:
        1 * eventBus.send(DEFAULT_ADDRESS, authInfo, _ as Handler<AsyncResult<Message>>) >> { address, info, callback ->
            callback.handle(failed(causeMessage))
        }
    }

    private AsyncResult<Message<Object>> failed(String causeMessage) {
        AsyncResult<Message<Object>> receivedUsers = Mock(AsyncResult.class);
        receivedUsers.succeeded() >> false;
        receivedUsers.cause() >> new Throwable(causeMessage);

        return receivedUsers;
    }

    private AsyncResult<Message<Object>> succeeded() {
        AsyncResult<Message<Object>> receivedUsers = Mock(AsyncResult.class);
        receivedUsers.succeeded() >> true;

        return receivedUsers;
    }
}
