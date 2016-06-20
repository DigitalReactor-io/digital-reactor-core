package io.digitalreactor.core.gateway.api;

import io.digitalreactor.core.SummaryDispatcherVerticle;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by MStepachev on 20.06.2016.
 */
public class AdminApiController {

    private Router router;
    private EventBus eventBus;

    public AdminApiController(Vertx vertx) {
        eventBus = vertx.eventBus();
        router = Router.router(vertx);
        router.route(HttpMethod.GET, "/createSummaryWithAllReport").handler(this::createSummaryWithAllReport);
        router.route(HttpMethod.GET, "/createSummaryByProjectId").handler(this::createSummaryByProjectId);
    }

    public Router router() {
        return router;
    }

    public void createSummaryWithAllReport(RoutingContext routingContext) {
        String token = routingContext.request().getParam("token");
        String counterId = routingContext.request().getParam("counterId");

        String summaryId = UUID.randomUUID().toString();
        List<ReportTypeEnum> requireReports = new ArrayList<>();
        requireReports.add(ReportTypeEnum.REFERRING_SOURCE);
        requireReports.add(ReportTypeEnum.SEARCH_PHRASE_YANDEX_DIRECT);
        requireReports.add(ReportTypeEnum.VISITS_DURING_MONTH);

        CreateSummaryMessage createSummaryMessage = new CreateSummaryMessage(token, counterId, summaryId, requireReports, new ArrayList<>());

        eventBus.send(SummaryDispatcherVerticle.CREATE_SUMMARY, Json.encode(createSummaryMessage));

        routingContext.response().end(summaryId);
    }


    public void createSummaryByProjectId(RoutingContext routingContext) {
        String projectId = routingContext.request().getParam("projectId");

        eventBus.send(SummaryDispatcherVerticle.CREATE_SUMMARY_BY_PROJECT_ID, new JsonObject().put("projectId", projectId));

        routingContext.response().end("send");
    }

}
