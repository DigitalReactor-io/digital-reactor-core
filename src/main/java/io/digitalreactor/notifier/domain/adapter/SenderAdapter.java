package io.digitalreactor.notifier.domain.adapter;

import io.digitalreactor.notifier.domain.Notification;

/**
 * Created by ingvard on 03.06.2016.
 */
public interface SenderAdapter {
    String name();

    void send(String transport, Notification notification);
}
