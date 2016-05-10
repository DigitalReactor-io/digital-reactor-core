package io.digitalreactor.core;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.VertxException;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.auth.jdbc.impl.JDBCUser;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private final String insertUser = "INSERT INTO " + USER_TABLE + " (id, email, password, registration_date) VALUES (DEFAULT, ?, ?, ?) RETURNING id";
    private final String userByEmail = "SELECT * FROM " + USER_TABLE + " WHERE email=?";
    private final String insertAccess = "INSERT INTO " + ACCESS_TABLE + " (id, token, user_id) VALUES (DEFAULT, ?, ?) RETURNING id";
    private final String insertProject = "INSERT INTO " + PROJECT_TABLE + " (id, project_name, counter_id, access_id) VALUES (DEFAULT, ?, ?, ?) RETURNING id";

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    private final int ID_POSITION_IN_TABLE = 0;
    private final int EMAIL_POSITION_IN_TABLE = 1;
    private final int PASSWORD_POSITION_IN_TABLE = 2;

    //TODO[St.maxim] to env
    private final String salt = "zScs23fs";

    @Override
    public void start() throws Exception {
        eventBus = vertx.eventBus();

        //TODO[St.maxim] to env
        JsonObject postgreSQLClientConfig = new JsonObject()
                .put("host", "horton.elephantsql.com")
                .put("port", 5432)
                .put("username", "skdqqjmf")
                .put("password", "OQ2JEategLWNQzfl9sNY-duW7x6N4WY0")
                .put("database", "skdqqjmf");

        postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);

        eventBus.consumer(NEW_USER, this::newUser);
        eventBus.consumer(AUTHENTICATE, this::authenticate);
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

        String password = passwordHash("123456", salt);

        postgreSQLClient.getConnection(res -> {
            if (res.succeeded()) {

                SQLConnection connection = res.result();

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

    /**
     * This code is the partial of implementation copy of io.vertx.ext.auth.jdbc.impl
     */
    private void authenticate(Message authInfo) {
        JsonObject userCredentials = (JsonObject) authInfo.body();

        String username = userCredentials.getString("username");
        if (username == null) {
            authInfo.fail(404, "authInfo must contain username in \'username\' field");
        } else {
            String password = userCredentials.getString("password");
            if (password == null) {
                authInfo.fail(404, "authInfo must contain password in \'password\' field");
            } else {
                //TODO[St.maxim] common code into function
                postgreSQLClient.getConnection(res -> {
                    if (res.succeeded()) {
                        SQLConnection connection = res.result();

                        connection.queryWithParams(userByEmail, (new JsonArray()).add(username), userResult -> {
                            if (userResult.succeeded()) {
                                ResultSet result = userResult.result();

                                switch (result.getNumRows()) {
                                    case 0:
                                        authInfo.fail(404, "Invalid username/password");
                                        break;
                                    case 1:
                                        JsonArray userRow = (JsonArray) result.getResults().get(0);
                                        String storedPassword = userRow.getString(PASSWORD_POSITION_IN_TABLE);

                                        String passwordHash = passwordHash(password, salt);

                                        if (storedPassword.equals(passwordHash)) {
                                            authInfo.reply(new JsonObject()
                                                    .put("id", userRow.getInteger(ID_POSITION_IN_TABLE))
                                                    .put("email", userRow.getString(EMAIL_POSITION_IN_TABLE))
                                            );
                                        } else {
                                            authInfo.fail(404, "Invalid username/password");
                                        }

                                        break;
                                    default:
                                        authInfo.fail(500, "Failure in authentication");
                                }

                            } else {
                                authInfo.fail(0, res.cause().getMessage());
                            }
                        });

                        connection.close((closeRes) -> {
                        });
                    } else {
                        authInfo.fail(0, res.cause().getMessage());
                    }

                });
            }
        }

    }

    private String passwordHash(String password, String salt) {
        try {
            MessageDigest hashAlgorithm = MessageDigest.getInstance("SHA-512");
            String contact = (salt == null ? "" : salt) + password;
            byte[] passwordHash = hashAlgorithm.digest(contact.getBytes(StandardCharsets.UTF_8));

            return bytesToHex(passwordHash);
        } catch (NoSuchAlgorithmException e) {
            throw new VertxException(e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        char[] chars = new char[bytes.length * 2];

        for (int i = 0; i < bytes.length; ++i) {
            int x = 255 & bytes[i];
            chars[i * 2] = HEX_CHARS[x >>> 4];
            chars[1 + i * 2] = HEX_CHARS[15 & x];
        }

        return new String(chars);
    }

}
