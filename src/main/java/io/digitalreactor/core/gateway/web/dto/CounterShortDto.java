package io.digitalreactor.core.gateway.web.dto;

/**
 * Created by ingvard on 02.05.16.
 */
public class CounterShortDto {
    private long id;
    private String name;

    public CounterShortDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
