package io.digitalreactor.core;

import io.digitalreactor.core.domain.SummaryDispatcher;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import io.vertx.core.eventbus.EventBus;

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
            summaryDispatcher.createSummary(toObj((String) msg.body(), CreateSummaryMessage.class));
        });

        eventBus.consumer(ENRICH_SUMMARY, msg -> {
            summaryDispatcher.enrichSummary(toObj((String) msg.body(), ReportMessage.class));
        });

        this.summaryDispatcher = new SummaryDispatcher(new SummaryDispatcherPublisher() {
            @Override
            public void createReport(
                    String counterId,
                    String clientToken,
                    String summaryId,
                    List<ReportTypeEnum> necessaryReports
            ) {
                necessaryReports.forEach(reportTypeEnum -> {
                    ReportMessage reportMessage = new ReportMessage();
                    reportMessage.clientToken = counterId;
                    reportMessage.summaryId = summaryId;
                    reportMessage.counterId = counterId;
                    reportMessage.reportType = reportTypeEnum;

                    eventBus.publish(MetricsLoaderVerticle.LOAD_REPORT, fromObj(reportMessage));
                });
            }

            @Override
            public void summaryWasCreated(String summaryId, List<String> callbackAddresses) {
                //TODO[st.maxim] implementation
            }
        });
    }

}
