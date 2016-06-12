package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceReportDto;
import io.digitalreactor.core.gateway.api.dto.VisitsDuringMonthReportDto;
import io.vertx.core.json.JsonObject;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public class ReportCreatorImpl implements ReportCreator {
    private final ObjectMapper mapper = JsonFactory.create();

    @Override
    public ReferringSourceReportDto createReferringSourceReport(ReportMessage reportMessage) {
        JsonObject object = new JsonObject(reportMessage.raw);

        return null;
    }

    @Override
    public VisitsDuringMonthReportDto createVisitsDuringMothReport(ReportMessage reportMessage) {
        JsonObject object = new JsonObject(reportMessage.raw);
        int total = ((Double) ((List) object.getJsonArray("totals").getList().get(0)).get(0)).intValue();
        List<Double> metrics = (List) object.getJsonArray("data").getJsonObject(0).getJsonArray("metrics").getList().get(0);
        String date = object.getJsonObject("query").getString("date1");

        List<Integer> visits = metrics.stream().map(aDouble -> aDouble.intValue()).collect(Collectors.toList());

        return new VisitsDuringMonthReportDto(
                total, 0, null,
                VisitsDuringMonthReportDto.visitsListWithDay(visits, LocalDate.parse(date)),
                null
        );
    }

    @Override
    public VisitsDuringMonthReportDto createSearchPhraseYandexReport(ReportMessage reportMessage) {
        return null;
    }
}
