package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.CreateSummaryMessage;
import io.digitalreactor.core.domain.publishers.SummaryDispatcherPublisher;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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

        summaryDispatcher.enrichSummary(SUMMARY_ID, currentReport);

        verify(summaryDispatcherPublisher).summaryWasCreated(SUMMARY_ID, callbackAddresses);
    }

    @Test
    public void enrichSummary_onlyOneReportWasCompleted_notificationDoesNotSent() {
        necessaryReports.add(ReportTypeEnum.VISITS_DURING_MONTH);
        necessaryReports.add(ReportTypeEnum.REFERRING_SOURCE);
        CreateSummaryMessage createSummaryMessage = createSummaryMessage();
        summaryDispatcher.createSummary(createSummaryMessage);

        summaryDispatcher.enrichSummary(SUMMARY_ID, ReportTypeEnum.VISITS_DURING_MONTH);

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
