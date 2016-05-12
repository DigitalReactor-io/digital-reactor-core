package io.digitalreactor.core.domain.messages;

import io.digitalreactor.core.domain.ReportTypeEnum;

/**
 * Created by ingvard on 07.04.16.
 */
public class ReportMessage {
    public String summaryId;
    public String clientToken;
    public String counterId;
    public ReportTypeEnum reportType;
    public String raw;
    public String report;

    public String json() {
        return raw;
    }
}
