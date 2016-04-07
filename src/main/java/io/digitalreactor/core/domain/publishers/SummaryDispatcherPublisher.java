package io.digitalreactor.core.domain.publishers;

import io.digitalreactor.core.domain.ReportTypeEnum;

import java.util.List;

/**
 * Created by ingvard on 07.04.16.
 */
public interface SummaryDispatcherPublisher {
    void createReport(String counterId, String clientToken, String summaryId, List<ReportTypeEnum> necessaryReports);

    void summaryWasCreated(String summaryId, List<String> callbackAddresses);
}
