package io.digitalreactor.core.domain.model;

/**
 * Created by MStepachev on 13.05.2016.
 */
public class ProjectStatus {
    private ProjectType type;
    private SummaryShort current;

    public ProjectStatus(ProjectType type) {
        this.type = type;
    }

    public ProjectStatus(ProjectType type, SummaryShort current) {
        this.type = type;
        this.current = current;
    }

    public ProjectType getType() {
        return type;
    }

    public SummaryShort getCurrent() {
        return current;
    }

    public enum ProjectType {
        CREATING, LOADING, ERROR, COMPLETED
    }
}
