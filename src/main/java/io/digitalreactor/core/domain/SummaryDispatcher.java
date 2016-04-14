package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryDispatcher {

    private class SummaryCheckList {
        private HashMap<ReportTypeEnum, Boolean> checkList = new HashMap<>();
        private List<String> callbackAddresses;

        public SummaryCheckList(List<ReportTypeEnum> necessaryReports, List<String> callbackAddresses) {
            this.callbackAddresses = callbackAddresses;
            for (ReportTypeEnum report : necessaryReports) {
                checkList.put(report, false);
            }
        }

        public void markAsSuccess(ReportTypeEnum report) {
            checkList.put(report, true);
        }

        public boolean isReady() {
            return !checkList.values().contains(false);
        }

        public List<String> getCallbackAddresses() {
            return callbackAddresses;
        }
    }

    private HashMap<String, SummaryCheckList> summaryStateStorage = new HashMap<>();
    private SummaryDispatcherPublisher summaryDispatcherPublisher;

    public SummaryDispatcher(SummaryDispatcherPublisher summaryDispatcherPublisher) {
        this.summaryDispatcherPublisher = summaryDispatcherPublisher;
    }

    public void createSummary(CreateSummaryMessage summary) {

        if (summary.necessaryReports.isEmpty()) {
            throw new IllegalStateException("The summary must have at least one report.");
        }

        summaryStateStorage.put(summary.summaryId, new SummaryCheckList(summary.necessaryReports, summary.callbackAddresses));

        summaryDispatcherPublisher.createReport(
                summary.counterId,
                summary.clientToken,
                summary.summaryId,
                summary.necessaryReports
        );
    }

    public void enrichSummary(String summaryId, ReportTypeEnum successReport) {
        //TODO[st.maxim] тут могут быть проблемы при многопоточном доступе
        SummaryCheckList summaryCheckList = summaryStateStorage.get(summaryId);
        summaryCheckList.markAsSuccess(successReport);
        summaryStateStorage.put(summaryId, summaryCheckList);

        if (summaryCheckList.isReady()) {
            summaryDispatcherPublisher.summaryWasCreated(summaryId, summaryCheckList.getCallbackAddresses());
        }
    }
}
