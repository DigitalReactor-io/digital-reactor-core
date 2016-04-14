package io.digitalreactor.core.domain.messages;

import io.digitalreactor.core.domain.ReportTypeEnum;

import java.util.List;

/**
 * Created by ingvard on 07.04.16.
 */
public class CreateSummaryMessage {
    public String clientToken;
    public String counterId;
    public String summaryId;
    public List<ReportTypeEnum> necessaryReports;
    public List<String> callbackAddresses;

    public CreateSummaryMessage(
            String clientToken,
            String counterId,
            String summaryId,
            List<ReportTypeEnum> necessaryReports,
            List<String> callbackAddresses
    ) {
        this.clientToken = clientToken;
        this.counterId = counterId;
        this.summaryId = summaryId;
        this.necessaryReports = necessaryReports;
        this.callbackAddresses = callbackAddresses;
    }
}
