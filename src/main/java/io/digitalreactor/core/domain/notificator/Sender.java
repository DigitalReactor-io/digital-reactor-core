package io.digitalreactor.core.domain.notificator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MStepachev on 16.05.2016.
 */
public class Sender {

    private Map<String, SenderAdapter> handlers = new HashMap<>();

    public void handle(Message message) {
        SenderAdapter adapter = handlers.get(message.transport());
        if (adapter != null) {
            adapter.send(message);
        } else {
            throw new RuntimeException("SenderAdapter " + message.transport() + " isn't loaded");
        }
    }

    public void registerSendHandler(SenderAdapter adapter) {
        handlers.put(adapter.transport(), adapter);
    }
}
