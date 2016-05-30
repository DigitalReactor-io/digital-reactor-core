package io.digitalreactor.core.gateway.api.dto;

import java.util.List;

/**
 * Created by MStepachev on 12.05.2016.
 */
public class ReferringSourceDto {
    private String name;
    private int visit;
    private int visitChange;
    private int goals;
    private int goalsChange;
    private double conversion;
    private double conversionChange;
    private List<VisitDto> metrics;

    public ReferringSourceDto(
            String name,
            int visit,
            int visitChange,
            int goals,
            int goalsChange,
            double conversion,
            double conversionChange,
            List<VisitDto> metrics
    ) {
        this.name = name;
        this.visit = visit;
        this.visitChange = visitChange;
        this.goals = goals;
        this.goalsChange = goalsChange;
        this.conversion = conversion;
        this.conversionChange = conversionChange;
        this.metrics = metrics;
    }

    public String getName() {
        return name;
    }

    public int getVisit() {
        return visit;
    }

    public int getVisitChange() {
        return visitChange;
    }

    public int getGoals() {
        return goals;
    }

    public int getGoalsChange() {
        return goalsChange;
    }

    public double getConversion() {
        return conversion;
    }

    public double getConversionChange() {
        return conversionChange;
    }

    public List<VisitDto> getMetrics() {
        return metrics;
    }
}
