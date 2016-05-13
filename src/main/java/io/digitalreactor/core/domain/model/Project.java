package io.digitalreactor.core.domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by MStepachev on 13.05.2016.
 */
public class Project {
    private int _id;
    private ProjectStatus status;
    private List<SummaryShort> history;

    public Project(int _id) {
        this.status = new ProjectStatus(ProjectStatus.ProjectType.CREATING);
        this._id = _id;
        this.history = new ArrayList<>();
    }

    public int get_id() {
        return _id;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public List<SummaryShort> getHistory() {
        return history;
    }
}
