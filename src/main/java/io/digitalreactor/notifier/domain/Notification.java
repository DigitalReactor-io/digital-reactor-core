package io.digitalreactor.notifier.domain;

/**
 * Created by ingvard on 03.06.2016.
 */
public class Notification<T extends NotificationEvent> {
    private Long userId;
    private T event;

    public Notification(Long userId, T event) {
        this.userId = userId;
        this.event = event;
    }

    public Long getUserId() {
        return userId;
    }

    public String eventType() {
        return event.name();
    }
}
