package io.digitalreactor.core.domain.messages;

import io.digitalreactor.core.domain.ReportTypeEnum;

/**
 * Created by ingvard on 07.04.16.
 */
public class LoadReportDataMessage {
    private String summaryId;
    private String counterId;
    private String clientToken;
    private ReportTypeEnum reportType;

    public String getSummaryId() {
        return summaryId;
    }

    public LoadReportDataMessage setSummaryId(String summaryId) {
        this.summaryId = summaryId;
        return this;
    }

    public String getCounterId() {
        return counterId;
    }

    public LoadReportDataMessage setCounterId(String counterId) {
        this.counterId = counterId;
        return this;
    }

    public String getClientToken() {
        return clientToken;
    }

    public LoadReportDataMessage setClientToken(String clientToken) {
        this.clientToken = clientToken;
        return this;
    }

    public ReportTypeEnum getReportType() {
        return reportType;
    }

    public LoadReportDataMessage setReportType(ReportTypeEnum reportType) {
        this.reportType = reportType;
        return this;
    }

}
