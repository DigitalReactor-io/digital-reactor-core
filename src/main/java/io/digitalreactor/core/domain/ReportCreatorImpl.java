package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceDto;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceReportDto;
import io.digitalreactor.core.gateway.api.dto.VisitsDuringMonthReportDto;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public class ReportCreatorImpl implements ReportCreator {
    private final ObjectMapper mapper = JsonFactory.create();

    @Override
    public ReferringSourceReportDto createReferringSourceReport(ReportMessage reportMessage) {
        List<ReferringSourceDto> sources = new ArrayList<>();

        JsonObject object = new JsonObject(reportMessage.raw);
        final JsonArray data = object.getJsonArray("data");
        String date = object.getJsonObject("query").getString("date1");

        for (Map<String, List> map : (List<Map<String, List>>) data.getList()) {
            final Map<String, String> dimensions = (Map<String, String>) map.get("dimensions").get(0);
            final String name = dimensions.get("name");
            final String id = dimensions.get("id");
            final List<Double> metrics = (List<Double>) map.get("metrics").get(0);
            List<Integer> visits = metrics.stream().map(aDouble -> aDouble.intValue()).collect(Collectors.toList());

            final int count = metrics.stream().mapToInt(Double::intValue).sum();
            final ReferringSourceDto referringSourceDto = new ReferringSourceDto(
                    name, count, 0, 0, 0, 0, 0,
                    VisitsDuringMonthReportDto.visitsListWithDay(visits, LocalDate.parse(date))
            );
            sources.add(referringSourceDto);
        }
        return new ReferringSourceReportDto(sources, 0, 0, 0, null);
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
