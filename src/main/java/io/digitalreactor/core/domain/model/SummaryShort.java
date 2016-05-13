package io.digitalreactor.core.domain.model;

/**
 * Created by MStepachev on 13.05.2016.
 */
public class SummaryShort {
    private String id;
    private String date;

    public SummaryShort(String id) {
        this.id = id;
    }

    public SummaryShort(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
}
