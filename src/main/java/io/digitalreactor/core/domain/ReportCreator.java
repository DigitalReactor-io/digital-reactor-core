package io.digitalreactor.core.domain;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.digitalreactor.core.gateway.api.dto.ReferringSourceReportDto;
import io.digitalreactor.core.gateway.api.dto.SearchPhraseYandexDirectDto;
import io.digitalreactor.core.gateway.api.dto.VisitsDuringMonthReportDto;

/**
 * Created by ingvard on 07.04.16.
 */
public interface ReportCreator {

    VisitsDuringMonthReportDto createVisitsDuringMothReport(ReportMessage reportMessage);

    ReferringSourceReportDto createReferringSourceReport(ReportMessage reportMessage);

    SearchPhraseYandexDirectDto createSearchPhraseYandexReport(ReportMessage reportMessage);
}
