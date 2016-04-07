package io.digitalreactor.core;

import io.digitalreactor.core.domain.SummaryDispatcher;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryDispatcherVerticle extends AbstractVerticle {
    public static final String CREATE_SUMMARY = "report.dispatcher.create_summary";
    public static final String ENRICH_SUMMARY = "report.dispatcher.enrich_summary";

    private SummaryDispatcher summaryDispatcher;

    public SummaryDispatcherVerticle() {
        this.summaryDispatcher = new SummaryDispatcher(new SummaryDispatcherPublisher() {
            @Override
            public void createReport(
                    String counterId,
                    String clientToken,
                    String summaryId,
                    List<ReportTypeEnum> necessaryReports
            ) {
                //TODO[st.maxim] implementation
            }

            @Override
            public void summaryWasCreated(String summaryId, List<String> callbackAddresses) {
                //TODO[st.maxim] implementation
            }
        });
    }

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();
        eventBus.consumer(CREATE_SUMMARY, this::createSummary);
        eventBus.consumer(ENRICH_SUMMARY, this::enrichSummary);
    }

    private void createSummary(Message message) {
        summaryDispatcher.createSummary(createSummaryMessage(message));
    }

    private void enrichSummary(Message message) {
        String summaryId = "1234";
        ReportTypeEnum successReport = ReportTypeEnum.VISITS_DURING_MONTH;
        summaryDispatcher.enrichSummary(summaryId, successReport);
    }

    private CreateSummaryMessage createSummaryMessage(Message message) {
        String counterId = "12345";
        String clientToken = "some token";
        String summaryId = "1234";
        List<ReportTypeEnum> necessaryReports = new ArrayList<>();
        List<String> callbackAddresses = new ArrayList<>();

        return new CreateSummaryMessage(
                clientToken,
                counterId,
                summaryId,
                necessaryReports,
                callbackAddresses
        );
    }

}
