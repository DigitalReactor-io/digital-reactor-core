package io.digitalreactor.core.gateway.api.dto;

import java.time.LocalTime;

/**
 * Created by MStepachev on 12.05.2016.
 */
public class SearchPhraseDto {
    private String phrase;
    private int visits;
    private double refusals;
    private double viewDepth;
    private LocalTime timeOnSite;
    private double conversion;

    public SearchPhraseDto(String phrase, int visits, double refusals, double viewDepth, LocalTime timeOnSite, double conversion) {
        this.phrase = phrase;
        this.visits = visits;
        this.refusals = refusals;
        this.viewDepth = viewDepth;
        this.timeOnSite = timeOnSite;
        this.conversion = conversion;
    }

    public String getPhrase() {
        return phrase;
    }

    public int getVisits() {
        return visits;
    }

    public double getRefusals() {
        return refusals;
    }

    public double getViewDepth() {
        return viewDepth;
    }

    public LocalTime getTimeOnSite() {
        return timeOnSite;
    }

    public double getConversion() {
        return conversion;
    }
}
