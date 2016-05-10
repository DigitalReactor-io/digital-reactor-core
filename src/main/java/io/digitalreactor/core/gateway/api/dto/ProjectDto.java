package io.digitalreactor.core.gateway.api.dto;

/**
 * Created by ingvard on 06.04.16.
 */
public class ProjectDto {
    private int id;
    private int counterId;
    private String name;
    private String lastUpdate;

    public ProjectDto(int id, int counterId, String name, String lastUpdate) {
        this.id = id;
        this.counterId = counterId;
        this.name = name;
        this.lastUpdate = lastUpdate;
    }

    public int getId() {
        return id;
    }

    public int getCounterId() {
        return counterId;
    }

    public String getName() {
        return name;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
