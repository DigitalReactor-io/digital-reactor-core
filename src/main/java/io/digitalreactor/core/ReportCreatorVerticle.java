package io.digitalreactor.core;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static io.digitalreactor.core.domain.ReportTypeEnum.*;

/**
 * Created by ingvard on 07.04.16.
 */
public class ReportCreatorVerticle extends ReactorAbstractVerticle {

    public static final String CREATE_REPORT = "report.creator.create_report";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_REPORT, msg -> {
            ReportMessage message = toObj(msg, ReportMessage.class);
            if (VISITS_DURING_MONTH.equals(message.reportType)) {
                message.report = toJson(mapVisits(new JsonObject(message.raw)));
            }
            if (REFERRING_SOURCE.equals(message.reportType)) {
                message.report = toJson(mapReferring(new JsonObject(message.raw)));
            }
            if (SEARCH_PHRASE_YANDEX_DIRECT.equals(message.reportType)) {

                SearchPhraseYandexDirectDto searchPhraseYandexDirectDto = new SearchPhraseYandexDirectDto(
                        "reason", null, null
                );
                message.report = toJson(searchPhraseYandexDirectDto);
            }

            eventBus.send(SummaryStorageVerticle.ENRICH, message, replayMsg -> {
                eventBus.send(SummaryDispatcherVerticle.ENRICH_SUMMARY, toJson(message));
            });

        });

    }

    private VisitsDuringMonthReportDto mapVisits(JsonObject jsonObject) {
        String date1 = jsonObject.getString("date1");
        LocalDate localDate = LocalDate.parse(date1, formatter);
        JsonObject data = jsonObject.getJsonArray("data").getJsonObject(0);
        JsonArray metrics = data.getJsonArray("metrics");
        List<Integer> metricsList = metrics.stream().map(in -> (Integer) in).collect(Collectors.toList());

        List<VisitDto> visitDtos = VisitsDuringMonthReportDto.visitsListWithDay(metricsList, localDate);

        //todo what???
        return new VisitsDuringMonthReportDto(
                metricsList.stream().mapToInt(i -> i).sum(),
                0,
                ActionEnum.INCREASING,
                visitDtos,
                "some reason"
        );
    }

    private ReferringSourceReportDto mapReferring(JsonObject jsonObject) {
        String date1 = jsonObject.getString("date1");
        LocalDate localDate = LocalDate.parse(date1, formatter);
        JsonArray array = jsonObject.getJsonArray("data");

        List<ReferringSourceDto> ms = array.stream().map((o) -> {
            JsonObject object = (JsonObject) o;
            JsonArray metrics = object.getJsonArray("metrics");
            return new ReferringSourceDto(null, 0, 0, 0, 0, 0, 0,
                    VisitsDuringMonthReportDto.visitsListWithDay(
                            metrics.stream()
                                    .map(in -> (Integer) in)
                                    .collect(Collectors.toList()),
                            localDate
                    )
            );
        }).collect(Collectors.toList());

        return new ReferringSourceReportDto(
                ms, 200, 123.0, 123.0, ActionEnum.INCREASING
        );
    }
}
