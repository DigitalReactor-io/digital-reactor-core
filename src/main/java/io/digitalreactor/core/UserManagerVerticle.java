package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ingvard on 02.05.16.
 */
public class UserManagerVerticle extends AbstractVerticle {

    private final static String BASE_USER_MANAGER = "digitalreactor.core.user.";
    public final static String NEW_USER = BASE_USER_MANAGER + "new";
    public final static String AUTHENTICATE = BASE_USER_MANAGER + "authenticate";

    private final String USER_TABLE = "users";
    private final String PROJECT_TABLE = "projects";
    private final String ACCESS_TABLE = "accesses";

    private EventBus eventBus;
    private AsyncSQLClient postgreSQLClient;

    @Override
    public void start() throws Exception {
        eventBus = vertx.eventBus();

        JsonObject postgreSQLClientConfig = new JsonObject()
                .put("host", "horton.elephantsql.com")
                .put("port", 5432)
                .put("username", "skdqqjmf")
                .put("password", "OQ2JEategLWNQzfl9sNY-duW7x6N4WY0")
                .put("database", "skdqqjmf");

        postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);

        eventBus.consumer(NEW_USER, this::newUser);
    }

    @Override
    public void stop() throws Exception {
        //TODO[St.maxim] check call
        postgreSQLClient.close();
    }

    private void newUser(Message newUserMessage) {

        JsonObject newUserJson = (JsonObject) newUserMessage.body();
        String email = newUserJson.getString("email");
        String token = newUserJson.getString("token");
        String name = newUserJson.getString("name");
        Long counterId = Long.valueOf(newUserJson.getString("counterId"));

        String password = "123456";

        postgreSQLClient.getConnection(res -> {
            if (res.succeeded()) {

                SQLConnection connection = res.result();

                String insertUser = "INSERT INTO " + USER_TABLE + " (id, email, password, registration_date) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
                String insertAccess = "INSERT INTO " + ACCESS_TABLE + " (id, token, user_id) VALUES (DEFAULT, ?, ?) RETURNING id";
                String insertProject = "INSERT INTO " + PROJECT_TABLE + " (id, project_name, counter_id, access_id) VALUES (DEFAULT, ?, ?, ?) RETURNING id";

                //TODO[St.maxim] callback hell
                connection.queryWithParams(insertUser,
                        new JsonArray().add(email).add(password).add(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))),
                        userInsertResult -> {
                            if (userInsertResult.succeeded()) {
                                int userId = userInsertResult.result().getResults().get(0).getInteger(0);

                                connection.queryWithParams(insertAccess,
                                        new JsonArray().add(token).add(userId),
                                        accessInsertResult -> {
                                            if (accessInsertResult.succeeded()) {
                                                int accessId = accessInsertResult.result().getResults().get(0).getInteger(0);

                                                connection.queryWithParams(insertProject,
                                                        new JsonArray().add(name).add(counterId).add(accessId),
                                                        projectInsertResult -> {
                                                            if (projectInsertResult.succeeded()) {
                                                                int projectId = projectInsertResult.result().getResults().get(0).getInteger(0);

                                                                newUserMessage.reply(projectId);
                                                            } else {
                                                                newUserMessage.fail(0, projectInsertResult.cause().getMessage());
                                                            }
                                                        });
                                            } else {
                                                newUserMessage.fail(0, accessInsertResult.cause().getMessage());
                                            }
                                        });
                            } else {
                                newUserMessage.fail(0, userInsertResult.cause().getMessage());
                            }
                        });

                connection.close((closeRes) -> {
                });

            } else {
                newUserMessage.fail(0, res.cause().getMessage());
            }
        });
    }

}
