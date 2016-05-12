package io.digitalreactor.core.gateway.api;

import io.digitalreactor.core.application.User;
import io.digitalreactor.core.gateway.api.dto.*;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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

        router.route(HttpMethod.GET, "/").handler(this::projectList);
        //TODO[St.maxim] implementation
        router.route(HttpMethod.GET, "/:id/summary/").handler(this::projectList);
        //TODO[St.maxim] implementation
        router.route(HttpMethod.GET, "/:id/summary/actual").handler(this::actualSummary);
    }

    public Router router() {
        return router;
    }

    private void actualSummary(RoutingContext routingContext) {

        List<Integer> visits = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));

        List<VisitDto> visitDtos = VisitsDuringMonthReportDto.visitsListWithDay(visits, LocalDate.now());

        VisitsDuringMonthReportDto visitReport = new VisitsDuringMonthReportDto(40, 40, ActionEnum.DECREASING, visitDtos, "some reason");


        SearchPhraseDto searchPhrase = new SearchPhraseDto(
                "test",
                12,
                12.3,
                23.2,
                LocalTime.now(),
                12.1
        );
        SearchPhraseYandexDirectDto searchPhrases = new SearchPhraseYandexDirectDto(
                "reason",
                new ArrayList<>(Arrays.asList(searchPhrase, searchPhrase, searchPhrase)),
                new ArrayList<>(Arrays.asList(searchPhrase, searchPhrase, searchPhrase))
        );

        ReferringSourceReportDto referringSourceReport = new ReferringSourceReportDto(
                new ArrayList<ReferringSourceDto>(Arrays.asList(
                        new ReferringSourceDto(
                                "test1",
                                11,
                                11,
                                11,
                                11,
                                1,
                                1,
                                visitDtos
                        ),
                        new ReferringSourceDto(
                                "test2",
                                11,
                                11,
                                11,
                                11,
                                1,
                                1,
                                visitDtos
                        )
                )),
                30,
                22.2,
                22.22
        );

        List<Object> v = new ArrayList<Object>();
        v.add(visitReport);
        v.add(referringSourceReport);
        v.add(searchPhrases);


        routingContext.response().end(Json.encode(new SummaryDto(v)));
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
