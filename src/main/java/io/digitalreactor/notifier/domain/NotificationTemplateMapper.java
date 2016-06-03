package io.digitalreactor.notifier.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ingvard on 03.06.2016.
 */
public abstract class NotificationTemplateMapper<T> {

    protected Map<String, TemplateHandler<T>> templateHandlers = new HashMap<>();

    public void registerHandler(String eventNameForHandel, TemplateHandler<T> handler) {
        templateHandlers.put(eventNameForHandel, handler);
    }

    public abstract T map(String transport, Notification notification);

}
