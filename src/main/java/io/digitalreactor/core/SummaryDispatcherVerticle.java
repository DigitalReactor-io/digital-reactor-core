package io.digitalreactor.core;

import io.digitalreactor.core.domain.SummaryDispatcher;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryDispatcherVerticle extends ReactorAbstractVerticle {
    public static final String CREATE_SUMMARY = "report.dispatcher.create_summary";
    public static final String ENRICH_SUMMARY = "report.dispatcher.enrich_summary";

    private SummaryDispatcher summaryDispatcher;

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_SUMMARY, msg -> {
            CreateSummaryMessage summaryMessage = toObj(msg, CreateSummaryMessage.class);
            eventBus.send(SummaryStorageVerticle.NEW, null, replayMsg -> {
                if (replayMsg.succeeded()){
                    summaryMessage.summaryId = ((JsonObject) replayMsg.result()).getString("summaryId");
                    summaryDispatcher.createSummary(summaryMessage);
                }
            });
        });


        eventBus.consumer(ENRICH_SUMMARY, msg -> {
            summaryDispatcher.enrichSummary(toObj(msg, ReportMessage.class));
        });

        this.summaryDispatcher = new SummaryDispatcher(new SummaryDispatcherPublisher() {
            @Override
            public void createReport(
                    String counterId,
                    String clientToken,
                    String summaryId,
                    List<ReportTypeEnum> necessaryReports
            ) {
                for (ReportTypeEnum type : necessaryReports) {
                    ReportMessage reportMessage = new ReportMessage();
                    reportMessage.clientToken = counterId;
                    reportMessage.summaryId = summaryId;
                    reportMessage.counterId = counterId;
                    reportMessage.reportType = type;

                    eventBus.send(MetricsLoaderVerticle.LOAD_REPORT, toJson(reportMessage));
                }

            }

            @Override
            public void summaryWasCreated(String summaryId, List<String> callbackAddresses) {
                callbackAddresses.forEach(address -> {
                    eventBus.send(address, summaryId);
                });
            }
        });
    }

}
