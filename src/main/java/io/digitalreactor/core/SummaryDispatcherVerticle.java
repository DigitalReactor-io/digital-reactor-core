package io.digitalreactor.core;

import io.digitalreactor.core.domain.SummaryDispatcher;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.model.Project;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryDispatcherVerticle extends ReactorAbstractVerticle {
    public static final String CREATE_SUMMARY = "report.dispatcher.create_summary";
    public static final String CREATE_SUMMARY_BY_PROJECT_ID = "report.dispatcher.create_summary_by_project_id";
    public static final String ENRICH_SUMMARY = "report.dispatcher.enrich_summary";

    private SummaryDispatcher summaryDispatcher;

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_SUMMARY, msg -> {
            summaryDispatcher.createSummary(toObj((String) msg.body(), CreateSummaryMessage.class));
        });

        eventBus.consumer(ENRICH_SUMMARY, msg -> {
            summaryDispatcher.enrichSummary(toObj((String) msg.body(), ReportMessage.class));
        });

        eventBus.consumer(CREATE_SUMMARY_BY_PROJECT_ID, this::createSummaryByProjectId);

        this.summaryDispatcher = new SummaryDispatcher(new SummaryDispatcherPublisher() {
            @Override
            public void createReport(
                    String counterId,
                    String clientToken,
                    String summaryId,
                    List<ReportTypeEnum> necessaryReports
            ) {
                eventBus.send(SummaryStorageVerticle.NEW, new JsonObject().put("summaryId", summaryId), reply -> {
                    if (reply.succeeded()) {
                        necessaryReports.forEach(reportTypeEnum -> {
                            ReportMessage reportMessage = new ReportMessage();
                            reportMessage.clientToken = clientToken;
                            reportMessage.summaryId = summaryId;
                            reportMessage.counterId = counterId;
                            reportMessage.reportType = reportTypeEnum;

                            eventBus.publish(MetricsLoaderVerticle.LOAD_REPORT, fromObj(reportMessage));
                        });
                    }
                });

            }

            @Override
            public void summaryWasCreated(String summaryId, List<String> callbackAddresses) {
                System.out.println("Summary was created: " + summaryId);
                //TODO[st.maxim] very bad solution
                String tokens[] = summaryId.split("_");

                eventBus.publish(
                        ProjectManagerVerticle.MARK_AS_COMPLETED,
                        new JsonObject()
                                .put("projectId", tokens[0])
                                .put("summaryId", summaryId)
                                .put("date", DateTime.now().toString())
                );
            }
        });
    }

    private void createSummaryByProjectId(Message message) {
        String projectId = ((JsonObject) message.body()).getString("projectId");
        vertx.eventBus().send(ProjectManagerVerticle.NEW_PROJECT, new JsonObject().put("id", projectId));

        vertx.eventBus().send(UserManagerVerticle.PROJECT_BY_ID, ((JsonObject) message.body()), reply -> {
            JsonObject param = ((JsonObject) reply.result().body());
            param.getLong("counterId");
            param.getString("token");

            String summaryTextId = projectId +"_"+ param.getLong("counterId") +"_"+ UUID.randomUUID().toString();

            List<ReportTypeEnum> requireReports = new ArrayList<>();
            requireReports.add(ReportTypeEnum.REFERRING_SOURCE);
            requireReports.add(ReportTypeEnum.SEARCH_PHRASE_YANDEX_DIRECT);
            requireReports.add(ReportTypeEnum.VISITS_DURING_MONTH);

            CreateSummaryMessage createSummaryMessage = new CreateSummaryMessage(
                    param.getString("token"),
                    String.valueOf(param.getLong("counterId")),
                    summaryTextId,
                    requireReports, new ArrayList<String>()
            );

            vertx.eventBus().publish(CREATE_SUMMARY, Json.encode(createSummaryMessage));
            vertx.eventBus().send(ProjectManagerVerticle.MARK_AS_LOADING, new JsonObject().put("projectId", projectId).put("summaryId", summaryTextId));
        });
    }

}
