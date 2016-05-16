package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryDispatcherTest {

    private final String CLIENT_TOKEN = "token";
    private final String COUNTER_ID = "counter id";
    private final String SUMMARY_ID = "summary id";

    private List<ReportTypeEnum> necessaryReports;
    private List<String> callbackAddresses;

    private SummaryDispatcher summaryDispatcher;
    private SummaryDispatcherPublisher summaryDispatcherPublisher;

    @Before
    public void setUp() throws Exception {
        summaryDispatcherPublisher = mock(SummaryDispatcherPublisher.class);
        summaryDispatcher = new SummaryDispatcher(summaryDispatcherPublisher);
        necessaryReports = new ArrayList<>();
        callbackAddresses = new ArrayList<>();
    }

    @Test(expected = IllegalStateException.class)
    public void createSummary_emptyNecessaryReports_exception() {
        CreateSummaryMessage createSummaryMessage = createSummaryMessage();

        summaryDispatcher.createSummary(createSummaryMessage);
    }

    @Test
    public void createSummary_createSummaryMessageWithNecessaryReport_createReportMessageWasSent() {
        necessaryReports.add(ReportTypeEnum.VISITS_DURING_MONTH);
        CreateSummaryMessage createSummaryMessage = createSummaryMessage();

        summaryDispatcher.createSummary(createSummaryMessage);

        verify(summaryDispatcherPublisher).createReport(COUNTER_ID, CLIENT_TOKEN, SUMMARY_ID, necessaryReports);
    }

    @Test
    public void enrichSummary_lastNecessaryReport_sendNotificationByCallbackAddresses() {
        ReportTypeEnum currentReport = ReportTypeEnum.VISITS_DURING_MONTH;
        necessaryReports.add(currentReport);
        CreateSummaryMessage createSummaryMessage = createSummaryMessage();
        summaryDispatcher.createSummary(createSummaryMessage);

        ReportMessage reportMessage = new ReportMessage();
        reportMessage.summaryId = createSummaryMessage.summaryId;
        reportMessage.reportType = currentReport;

        summaryDispatcher.enrichSummary(reportMessage);

        verify(summaryDispatcherPublisher).summaryWasCreated(SUMMARY_ID, callbackAddresses);
    }

    @Test
    public void enrichSummary_onlyOneReportWasCompleted_notificationDoesNotSent() {
        necessaryReports.add(ReportTypeEnum.VISITS_DURING_MONTH);
        necessaryReports.add(ReportTypeEnum.REFERRING_SOURCE);
        CreateSummaryMessage createSummaryMessage = createSummaryMessage();
        summaryDispatcher.createSummary(createSummaryMessage);

        ReportMessage reportMessage1 = new ReportMessage();
        reportMessage1.summaryId = createSummaryMessage.summaryId;
        reportMessage1.reportType = ReportTypeEnum.VISITS_DURING_MONTH;

        ReportMessage reportMessage2 = new ReportMessage();
        reportMessage2.summaryId = createSummaryMessage.summaryId;
        reportMessage2.reportType = ReportTypeEnum.VISITS_DURING_MONTH;

        summaryDispatcher.enrichSummary(reportMessage1);
        summaryDispatcher.enrichSummary(reportMessage2);

        verify(summaryDispatcherPublisher, never()).summaryWasCreated(any(), any());
    }

    private CreateSummaryMessage createSummaryMessage() {
        return new CreateSummaryMessage(
                CLIENT_TOKEN,
                COUNTER_ID,
                SUMMARY_ID,
                necessaryReports,
                callbackAddresses
        );
    }
}
