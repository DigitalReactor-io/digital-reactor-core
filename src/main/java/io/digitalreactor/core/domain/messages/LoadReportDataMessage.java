package io.digitalreactor.core.domain.messages;

import io.digitalreactor.core.domain.ReportTypeEnum;

/**
 * Created by ingvard on 07.04.16.
 */
public class LoadReportDataMessage {
    public String summaryId;
    public String counterId;
    public String clientToken;
    public ReportTypeEnum reportType;
}
