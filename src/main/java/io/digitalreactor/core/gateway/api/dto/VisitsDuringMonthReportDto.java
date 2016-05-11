package io.digitalreactor.core.gateway.api.dto;

import io.digitalreactor.core.domain.ReportTypeEnum;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MStepachev on 11.05.2016.
 */
public class VisitsDuringMonthReportDto {

    private ReportTypeEnum type = ReportTypeEnum.VISITS_DURING_MONTH;
    private int visit;
    private int percent;
    private ActionEnum action;
    private List<VisitDto> metrics;
    private String reason;

    public VisitsDuringMonthReportDto(int visit, int percent, ActionEnum action, List<VisitDto> metrics, String reason) {
        this.visit = visit;
        this.percent = percent;
        this.action = action;
        this.metrics = metrics;
        this.reason = reason;
    }

    public ReportTypeEnum getType() {
        return type;
    }

    public int getVisit() {
        return visit;
    }

    public double getPercent() {
        return percent;
    }

    public ActionEnum getAction() {
        return action;
    }

    public List<VisitDto> getMetrics() {
        return metrics;
    }

    public String getReason() {
        return reason;
    }

    //TODO[St.maxim] move to util class
    public static List<VisitDto> visitsListWithDay(List<Integer> visits, LocalDate startTime) {

        List<VisitDto> result = new ArrayList<VisitDto>();
        LocalDate pointerDate = startTime;

        for (int visit : visits) {
            result.add(new VisitDto(
                    visit,
                    pointerDate.toString(),
                    isHoliday(pointerDate) ? VisitDto.DayType.HOLIDAY : VisitDto.DayType.WEEKDAY
            ));
            pointerDate = pointerDate.plusDays(1);
        }

        return result;
    }

    private static boolean isHoliday(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
