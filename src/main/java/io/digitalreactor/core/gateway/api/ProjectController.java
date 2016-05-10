package io.digitalreactor.core.gateway.api;

import io.digitalreactor.core.application.User;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ingvard on 06.04.16.
 */
public class ProjectController {

    private AsyncSQLClient postgreSQLClient;

    public ProjectController(Vertx vertx) {
        //TODO[St.maxim] to env
        JsonObject postgreSQLClientConfig = new JsonObject()
                .put("host", "horton.elephantsql.com")
                .put("port", 5432)
                .put("username", "skdqqjmf")
                .put("password", "OQ2JEategLWNQzfl9sNY-duW7x6N4WY0")
                .put("database", "skdqqjmf");

        postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);
    }


    private void projectList(RoutingContext routingContext) {

        //TODO[St.maxim] extract to independent vertical such as "Project vertical"
        int userId = ((User) routingContext.user()).id();

        String SelectClientProjects = "SELECT project_name, counter_id, last_update FROM users AS u, accesses As a, projects AS p WHERE u.id = ? AND u.id = a.user_id AND a.id = p.access_id";

        postgreSQLClient.getConnection(res -> {

            if (res.succeeded()) {
                SQLConnection connection = res.result();

                connection.queryWithParams(SelectClientProjects, new JsonArray().add(userId), selectedClientProject -> {
                    if (selectedClientProject.succeeded()) {
                        List<JsonArray> results = selectedClientProject.result().getResults();

                        List<JsonObject> projects = new ArrayList<JsonObject>();

                        for (JsonArray project : results) {
                            projects.add(new JsonObject()
                                    .put("name", project.getString(0))
                                    .put("counterId", project.getInteger(1))
                                    .put("lastUpdate", project.getString(2))
                            );

                        }


                    } else {
                        routingContext.response().setStatusCode(500).end("connection problem");
                    }
                });

                connection.close(close -> {
                });

            } else {
                routingContext.response().setStatusCode(500).end("connection problem");
            }


        });
    }
}
