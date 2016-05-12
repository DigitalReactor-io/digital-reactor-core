package io.digitalreactor.core.gateway.api.dto;

import io.digitalreactor.core.domain.ReportTypeEnum;

import java.util.List;

/**
 * Created by MStepachev on 11.05.2016.
 */
public class ReferringSourceReportDto {
    private ReportTypeEnum type = ReportTypeEnum.REFERRING_SOURCE;
    private List<ReferringSourceDto> sources;
    private int totalGoals;
    private double totalConversion;
    private double totalConversionChange;
    private ActionEnum action;

    public ReferringSourceReportDto(List<ReferringSourceDto> sources, int totalGoals, double totalConversion, double totalConversionChange, ActionEnum action) {
        this.sources = sources;
        this.totalGoals = totalGoals;
        this.totalConversion = totalConversion;
        this.totalConversionChange = totalConversionChange;
        this.action = action;
    }

    public ActionEnum getAction() {
        return action;
    }

    public ReportTypeEnum getType() {
        return type;
    }

    public List<ReferringSourceDto> getSources() {
        return sources;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public double getTotalConversion() {
        return totalConversion;
    }

    public double getTotalConversionChange() {
        return totalConversionChange;
    }
}
