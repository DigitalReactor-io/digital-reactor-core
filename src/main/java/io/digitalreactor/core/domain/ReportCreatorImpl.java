package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.*;
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
    public SearchPhraseYandexDirectDto createSearchPhraseYandexReport(ReportMessage reportMessage) {
        JsonObject object = new JsonObject(reportMessage.raw);
        JsonArray phrasesWithMetrics = object.getJsonArray("data");
        String reason = "some_reason";

        return new SearchPhraseYandexDirectDto(
                reason,
                extractTenSuccessPhrases(phrasesWithMetrics),
                extractFailurePhrases(phrasesWithMetrics)
        );
    }

    private List<SearchPhraseDto> extractFailurePhrases(JsonArray phrasesWithMetrics) {
        List<SearchPhraseDto> searchPhraseDtos = new ArrayList<>();

        for (Object obj : phrasesWithMetrics) {
            String phrase = ((JsonObject) obj).getJsonArray("dimensions").getJsonObject(0).getString("name");
            int visits = ((JsonObject) obj).getJsonArray("metrics").getInteger(0);
            double pageDepth = ((JsonObject) obj).getJsonArray("metrics").getDouble(1);
            double avgVisitDurationSeconds = ((JsonObject) obj).getJsonArray("metrics").getDouble(2);
            double bounceRate = ((JsonObject) obj).getJsonArray("metrics").getDouble(3);

            searchPhraseDtos.add(new SearchPhraseDto(phrase, visits, bounceRate, pageDepth, avgVisitDurationSeconds, 0.0));
        }

        return searchPhraseDtos.size() > 10 ? searchPhraseDtos.subList(0, 10) : searchPhraseDtos;
    }

    private List<SearchPhraseDto> extractTenSuccessPhrases(JsonArray phrasesWithMetrics) {

        List<SearchPhraseDto> searchPhraseDtos = new ArrayList<>();

        for (Object obj : phrasesWithMetrics) {
            String phrase = ((JsonObject) obj).getJsonArray("dimensions").getJsonObject(0).getString("name");
            int visits = ((JsonObject) obj).getJsonArray("metrics").getInteger(0);
            double pageDepth = ((JsonObject) obj).getJsonArray("metrics").getDouble(1);
            double avgVisitDurationSeconds = ((JsonObject) obj).getJsonArray("metrics").getDouble(2);
            double bounceRate = ((JsonObject) obj).getJsonArray("metrics").getDouble(3);

            if (bounceRate > 50.0 && avgVisitDurationSeconds > 15.0) {
                searchPhraseDtos.add(new SearchPhraseDto(phrase, visits, bounceRate, pageDepth, avgVisitDurationSeconds, 0.0));
            }
        }

        searchPhraseDtos.sort((p1, p2) -> p1.getVisits() > p2.getVisits() ? 1 : p1.getVisits() == p2.getVisits() ? 0 : -1);

        return searchPhraseDtos.size() > 10 ? searchPhraseDtos.subList(0, 10) : searchPhraseDtos;
    }

    private double weightFunction(int visits, double pageDepth, double avgVisitDurationSeconds, double bounceRate) {
        return visits + bounceRate * 10 + pageDepth + avgVisitDurationSeconds / 6;
    }
}
