package io.digitalreactor.core;

import io.digitalreactor.core.api.yandex.Response;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.ActionEnum;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceReportDto;
import io.digitalreactor.core.gateway.api.dto.SearchPhraseYandexDirectDto;
import io.digitalreactor.core.gateway.api.dto.VisitsDuringMonthReportDto;
import io.vertx.core.eventbus.EventBus;

import static io.digitalreactor.core.domain.ReportTypeEnum.*;

/**
 * Created by ingvard on 07.04.16.
 */
public class ReportCreatorVerticle extends ReactorAbstractVerticle {

    public static final String CREATE_REPORT = "report.creator.create_report";

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_REPORT, msg -> {
            ReportMessage message = toObj(msg, ReportMessage.class);
            if (VISITS_DURING_MONTH.equals(message.reportType)) {

                VisitsDuringMonthReportDto visitsDuringMonthReportDto = new VisitsDuringMonthReportDto(
                        30, 10, ActionEnum.INCREASING, null, "some reason"
                );
                message.report = toJson(visitsDuringMonthReportDto);
            }
            if (REFERRING_SOURCE.equals(message.reportType)) {

                ReferringSourceReportDto referringSourceReportDto = new ReferringSourceReportDto(
                        null, 200, 123.0, 123.0, ActionEnum.INCREASING
                );
                message.report = toJson(referringSourceReportDto);
            }
            if (SEARCH_PHRASE_YANDEX_DIRECT.equals(message.reportType)) {

                SearchPhraseYandexDirectDto searchPhraseYandexDirectDto = new SearchPhraseYandexDirectDto(
                        "reason", null, null
                );
                message.report = toJson(searchPhraseYandexDirectDto);
            }

            eventBus.send(SummaryStorageVerticle.ENRICH,message,replayMsg->{
                eventBus.send(SummaryDispatcherVerticle.ENRICH_SUMMARY, toJson(message));
            });

        });

    }
}
