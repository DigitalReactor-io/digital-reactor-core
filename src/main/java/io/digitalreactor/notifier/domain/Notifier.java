package io.digitalreactor.notifier.domain;

import io.digitalreactor.notifier.domain.adapter.SenderAdapter;

import java.util.Map;

/**
 * Created by ingvard on 03.06.2016.
 */
public class Notifier {
    private Map<String, SenderAdapter> adapters;

    public void registerAdapter(SenderAdapter adapter) {
        adapters.put(adapter.name(), adapter);
    }

    public void send(Route route, Notification notification) {
        SenderAdapter adapter = adapters.get(route.getName());
        adapter.send(route.getTransport(), notification);
    }
}
