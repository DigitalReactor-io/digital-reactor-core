package io.digitalreactor.notifier.domain;

/**
 * Created by ingvard on 03.06.2016.
 */
public class Route {
    private String transport;
    private String name;

    public Route(String transport, String name) {
        this.transport = transport;
        this.name = name;
    }

    public String getTransport() {
        return transport;
    }

    public String getName() {
        return name;
    }
}
