package io.digitalreactor.core.gateway.api;

import io.digitalreactor.core.ProjectManagerVerticle;
import io.digitalreactor.core.SummaryDispatcherVerticle;
import io.digitalreactor.core.SummaryStorageVerticle;
import io.digitalreactor.core.application.User;
import io.digitalreactor.core.gateway.api.dto.ProjectDto;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ingvard on 06.04.16.
 */
public class ProjectApiController {

    private final int PROJECT_ID_POSITION = 0;
    private final int PROJECT_NAME_POSITION = 1;
    private final int COUNTER_ID_POSITION = 2;
    private final int LAST_UPDATE_POSITION = 3;

    private Router router;
    private AsyncSQLClient postgreSQLClient;
    private EventBus eventBus;

    public ProjectApiController(Vertx vertx) {
        //TODO[St.maxim] to env
        JsonObject postgreSQLClientConfig = new JsonObject()
                .put("host", "horton.elephantsql.com")
                .put("port", 5432)
                .put("username", "skdqqjmf")
                .put("password", "OQ2JEategLWNQzfl9sNY-duW7x6N4WY0")
                .put("database", "skdqqjmf");

        postgreSQLClient = PostgreSQLClient.createShared(vertx, postgreSQLClientConfig);
        router = Router.router(vertx);
        eventBus = vertx.eventBus();

        router.route(HttpMethod.GET, "/").handler(this::projectList);
        //TODO[St.maxim] implementation
        router.route(HttpMethod.GET, "/:id/summary/").handler(this::projectList);
        //TODO[St.maxim] implementation
        router.route(HttpMethod.GET, "/:id/summary/actual").handler(this::actualSummary);
        router.route(HttpMethod.PUT, "/:id/updateSummary").handler(this::updateSummary);
    }

    public Router router() {
        return router;
    }

    private void actualSummary(RoutingContext routingContext) {
        //TODO[St.maxim] check a user has this summary and get current summaryId
        String projectId = routingContext.request().getParam("id");

        eventBus.send(ProjectManagerVerticle.GET_BY_ID, new JsonObject().put("id", projectId), projectResult -> {

            if (projectResult.succeeded()) {
                String summaryId = ((JsonObject) projectResult.result().body()).getJsonObject("status").getJsonObject("current").getString("id");

                eventBus.send(SummaryStorageVerticle.GET_BY_ID, new JsonObject().put("summaryId", summaryId), summaryResult -> {
                    if (summaryResult.succeeded()) {
                        routingContext.response().end(summaryResult.result().body().toString());
                    } else {
                        routingContext.response().setStatusCode(500).end(summaryResult.cause().getMessage());
                    }
                });
            } else {
                routingContext.response().setStatusCode(500).end(projectResult.cause().getMessage());
            }

        });
    }

    private void updateSummary(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("id");

        eventBus.send(SummaryDispatcherVerticle.CREATE_SUMMARY_BY_PROJECT_ID, new JsonObject().put("projectId", projectId));

        routingContext.response().end("send");
    }

    private void projectList(RoutingContext routingContext) {

        //TODO[St.maxim] extract to independent vertical such as "Project vertical"
        int userId = ((User) routingContext.user()).id();

        String SelectClientProjects = "SELECT p.id, project_name, counter_id, last_update FROM users AS u, accesses As a, projects AS p WHERE u.id = ? AND u.id = a.user_id AND a.id = p.access_id";

        postgreSQLClient.getConnection(res -> {

            if (res.succeeded()) {
                SQLConnection connection = res.result();

                connection.queryWithParams(SelectClientProjects, new JsonArray().add(userId), selectedClientProject -> {
                    if (selectedClientProject.succeeded()) {
                        List<JsonArray> results = selectedClientProject.result().getResults();
                        List<ProjectDto> projects = new ArrayList<ProjectDto>();

                        for (JsonArray project : results) {
                            projects.add(new ProjectDto(
                                    project.getInteger(PROJECT_ID_POSITION),
                                    project.getInteger(COUNTER_ID_POSITION),
                                    project.getString(PROJECT_NAME_POSITION),
                                    project.getString(LAST_UPDATE_POSITION)
                            ));
                        }

                        routingContext.response().end(Json.encode(projects));
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
