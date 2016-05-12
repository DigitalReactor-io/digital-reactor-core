package io.digitalreactor.core.gateway.api.dto;

import io.digitalreactor.core.domain.ReportTypeEnum;

import java.util.List;

/**
 * Created by MStepachev on 12.05.2016.
 */
public class SearchPhraseYandexDirectDto {
    private ReportTypeEnum type = ReportTypeEnum.SEARCH_PHRASE_YANDEX_DIRECT;
    private String reason;
    private List<SearchPhraseDto> successPhrases;
    private List<SearchPhraseDto> failurePhrases;

    public SearchPhraseYandexDirectDto(String reason, List<SearchPhraseDto> successPhrases, List<SearchPhraseDto> failurePhrases) {
        this.reason = reason;
        this.successPhrases = successPhrases;
        this.failurePhrases = failurePhrases;
    }

    public ReportTypeEnum getType() {
        return type;
    }

    public String getReason() {
        return reason;
    }

    public List<SearchPhraseDto> getSuccessPhrases() {
        return successPhrases;
    }

    public List<SearchPhraseDto> getFailurePhrases() {
        return failurePhrases;
    }
}
