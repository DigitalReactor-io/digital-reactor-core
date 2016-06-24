package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by flaidzeres on 12.06.2016.
 */
public class ReportCreatorImpl implements ReportCreator {
    private final ObjectMapper mapper = JsonFactory.create();

    private final int TWO_WEEK = 14;

    @Override
    public ReferringSourceReportDto createReferringSourceReport(ReportMessage reportMessage) {
        List<ReferringSourceDto> sources = new ArrayList<>();

        JsonObject object = new JsonObject(reportMessage.raw);
        final JsonArray data = object.getJsonArray("data");
        String date1 = object.getJsonObject("query").getString("date1");
        String date2 = object.getJsonObject("query").getString("date2");

        for (Map<String, List> map : (List<Map<String, List>>) data.getList()) {
            final Map<String, String> dimensions = (Map<String, String>) map.get("dimensions").get(0);
            final String name = dimensions.get("name");
            final String id = dimensions.get("id");
            final List<Double> metrics = (List<Double>) map.get("metrics").get(0);
            List<Integer> visits = metrics.stream().map(aDouble -> aDouble.intValue()).collect(Collectors.toList());

            final int count = metrics.stream().mapToInt(Double::intValue).sum();
            int visitChange = 0;
            List<List<Double>> twoCalendarWeek = chooseValueOfWeekAndLastWeek(metrics, LocalDate.parse(date2));

            if (!twoCalendarWeek.isEmpty()) {
                double sumWeek1 = twoCalendarWeek.get(0).stream().mapToDouble(x -> x).sum();
                double sumWeek2 = twoCalendarWeek.get(1).stream().mapToDouble(x -> x).sum();
                visitChange = (int) (sumWeek1 - sumWeek2);
            }

            final ReferringSourceDto referringSourceDto = new ReferringSourceDto(
                    name, count, visitChange, 0, 0, 0, 0,
                    VisitsDuringMonthReportDto.visitsListWithDay(visits, LocalDate.parse(date1))
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
        String date2 = object.getJsonObject("query").getString("date2");
        List<Integer> visits = metrics.stream().map(aDouble -> aDouble.intValue()).collect(Collectors.toList());

        List<List<Double>> twoCalendarWeek = chooseValueOfWeekAndLastWeek(metrics, LocalDate.parse(date2));
        ActionEnum action = ActionEnum.INSUFFICIENT_DATA;
        int trendChangePercent = 0;

        if (!twoCalendarWeek.isEmpty()) {
            double sumWeek1 = twoCalendarWeek.get(0).stream().mapToDouble(x -> x).sum();
            double sumWeek2 = twoCalendarWeek.get(1).stream().mapToDouble(x -> x).sum();
            double delta = sumWeek1 - sumWeek2;
            action = trendChanges(delta);

            if (sumWeek2 > sumWeek1) {
                trendChangePercent = (int) ((((double) sumWeek2 / (double) sumWeek1) - 1.0) * 100);
            }

            if (sumWeek2 < sumWeek1) {
                trendChangePercent = (int) ((((double) sumWeek1 / (double) sumWeek2) - 1.0) * 100);
            }
        }

        return new VisitsDuringMonthReportDto(
                total, trendChangePercent, action,
                VisitsDuringMonthReportDto.visitsListWithDay(visits, LocalDate.parse(date)),
                "Текст для причины"
        );
    }

    public List<List<Double>> chooseValueOfWeekAndLastWeek(List<Double> metrics, LocalDate endReportDate) {

        List<List<Double>> valuesForTwoCalendarWeeks = new ArrayList<>();

        LocalDate endOfLastWeek = endReportDate.minus(1, ChronoUnit.WEEKS).with(DayOfWeek.SUNDAY);
        int rightShift = (int) DAYS.between(endOfLastWeek, endReportDate);

        if (metrics.size() - TWO_WEEK - rightShift >= 0) {
            valuesForTwoCalendarWeeks.add(metrics.subList(
                    metrics.size() - rightShift - 14,
                    metrics.size() - rightShift - 7
            ));
            valuesForTwoCalendarWeeks.add(metrics.subList(
                    metrics.size() - rightShift - 7,
                    metrics.size() - rightShift
            ));
        }

        return valuesForTwoCalendarWeeks;
    }

    @Override
    public SearchPhraseYandexDirectDto createSearchPhraseYandexReport(ReportMessage reportMessage) {
        JsonObject object = new JsonObject(reportMessage.raw);
        JsonArray phrasesWithMetrics = object.getJsonArray("data");
        String reason = "some_reason";

        return new SearchPhraseYandexDirectDto(
                reason,
                extractTenSuccessPhrases(phrasesWithMetrics),
                extractTenFailurePhrases(phrasesWithMetrics)
        );
    }


    private ActionEnum trendChanges(double delta) {
        ActionEnum action = ActionEnum.DECREASING;

        if (delta == 0) {
            action = ActionEnum.UNALTERED;
        } else if (delta > 0) {
            action = ActionEnum.INCREASING;
        }

        return action;
    }

    private List<SearchPhraseDto> extractTenFailurePhrases(JsonArray phrasesWithMetrics) {
        List<SearchPhraseDto> searchPhraseDtos = new ArrayList<>();

        for (Object obj : phrasesWithMetrics) {
            String phrase = ((JsonObject) obj).getJsonArray("dimensions").getJsonObject(0).getString("name");
            int visits = ((JsonObject) obj).getJsonArray("metrics").getInteger(0);
            double pageDepth = ((JsonObject) obj).getJsonArray("metrics").getDouble(1);
            double avgVisitDurationSeconds = ((JsonObject) obj).getJsonArray("metrics").getDouble(2);
            double bounceRate = ((JsonObject) obj).getJsonArray("metrics").getDouble(3);

            searchPhraseDtos.add(new SearchPhraseDto(phrase, visits, bounceRate, pageDepth, avgVisitDurationSeconds, 0.0));
        }

        searchPhraseDtos.sort((p1, p2) ->
                weightFunctionBySearchPhraseDto(p1) > weightFunctionBySearchPhraseDto(p2) ? 1 :
                        weightFunctionBySearchPhraseDto(p1) == weightFunctionBySearchPhraseDto(p2) ? 0 : -1
        );

        List<SearchPhraseDto> failure = searchPhraseDtos.size() > 10 ? searchPhraseDtos.subList(0, 10) : searchPhraseDtos;
        failure.sort((p1, p2) -> p1.getVisits() > p2.getVisits() ? 1 : p1.getVisits() == p2.getVisits() ? 0 : -1);

        return failure;
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

    private double weightFunctionBySearchPhraseDto(SearchPhraseDto searchPhraseDto) {
        return weightFunction(
                searchPhraseDto.getVisits(),
                searchPhraseDto.getViewDepth(),
                searchPhraseDto.getTimeOnSite(),
                searchPhraseDto.getBounceRate()
        );
    }

    private double weightFunction(int visits, double pageDepth, double avgVisitDurationSeconds, double bounceRate) {
        return visits + bounceRate * 10 + pageDepth + avgVisitDurationSeconds / 6;
    }
}
