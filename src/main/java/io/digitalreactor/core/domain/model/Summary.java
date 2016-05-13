package io.digitalreactor.core.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MStepachev on 13.05.2016.
 */
public class Summary {
    private int _id;
    private List<Object> reports;

    public Summary(int _id) {
        this._id = _id;
        this.reports = new ArrayList<>();
    }

    public int get_id() {
        return _id;
    }

    public List<Object> getReports() {
        return reports;
    }
}
