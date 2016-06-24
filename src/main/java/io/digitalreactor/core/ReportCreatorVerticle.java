package io.digitalreactor.core;

import io.digitalreactor.core.domain.ReportCreator;
import io.digitalreactor.core.domain.ReportCreatorImpl;
import io.digitalreactor.core.domain.ReportTypeEnum;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by ingvard on 07.04.16.
 */
public class ReportCreatorVerticle extends ReactorAbstractVerticle {

    public static final String CREATE_REPORT = "report.creator.create_report";

    private final ReportCreator reportCreator = new ReportCreatorImpl();

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_REPORT, msg -> {
            ReportMessage message = toObj((String) msg.body(), ReportMessage.class);

            if (ReportTypeEnum.VISITS_DURING_MONTH.equals(message.reportType)) {
                message.report = mapper.toJson(reportCreator.createVisitsDuringMothReport(message));
            } else if (ReportTypeEnum.REFERRING_SOURCE.equals(message.reportType)) {
                message.report = mapper.toJson(reportCreator.createReferringSourceReport(message));
            } else if (ReportTypeEnum.SEARCH_PHRASE_YANDEX_DIRECT.equals(message.reportType)) {
                message.report = mapper.toJson(reportCreator.createSearchPhraseYandexReport(message));
            }

            eventBus.send(SummaryStorageVerticle.ENRICH, fromObj(message));

            eventBus.publish(SummaryDispatcherVerticle.ENRICH_SUMMARY, fromObj(message));
        });

    }
}
