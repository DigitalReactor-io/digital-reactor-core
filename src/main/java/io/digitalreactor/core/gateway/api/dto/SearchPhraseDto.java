package io.digitalreactor.core.gateway.api.dto;

import java.time.LocalTime;

/**
 * Created by MStepachev on 12.05.2016.
 */
public class SearchPhraseDto {
    private String phrase;
    private int visits;
    private double bounceRate;
    private double viewDepth;
    private double timeOnSite;
    private double conversion;

    public SearchPhraseDto(String phrase, int visits, double bounceRate, double viewDepth, double timeOnSite, double conversion) {
        this.phrase = phrase;
        this.visits = visits;
        this.bounceRate = bounceRate;
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

    public double getBounceRate() {
        return bounceRate;
    }

    public double getViewDepth() {
        return viewDepth;
    }

    public double getTimeOnSite() {
        return timeOnSite;
    }

    public double getConversion() {
        return conversion;
    }
}
