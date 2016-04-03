package io.digitalreactor.core.messages;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ingvard on 03.04.16.
 */
public class VisitReportDto {
    private int visit;
    private int percent;
    private TrendTypeEnum trendType;
    private List<Visit> metrics;
    private String reason;

    public VisitReportDto(int visit, int percent, TrendTypeEnum trendType, List<Visit> metrics, String reason) {
        this.visit = visit;
        this.percent = percent;
        this.trendType = trendType;
        this.metrics = metrics;
        this.reason = reason;
    }

    public int getVisit() {
        return visit;
    }

    public double getPercent() {
        return percent;
    }

    public TrendTypeEnum getTrendType() {
        return trendType;
    }

    public List<Visit> getMetrics() {
        return metrics;
    }

    public String getReason() {
        return reason;
    }

    public static List<Visit> visitsListWithDay(List<Integer> visits, LocalDate startTime) {

        List<Visit> result = new ArrayList<Visit>();
        LocalDate pointerDate = startTime;

        for (int visit : visits) {
            result.add(new Visit(
                    visit,
                    pointerDate.toString(),
                    isHoliday(pointerDate) ? Visit.DayType.HOLIDAY : Visit.DayType.WEEKDAY
            ));
            pointerDate = pointerDate.plusDays(1);
        }

        return result;
    }

    private static boolean isHoliday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
