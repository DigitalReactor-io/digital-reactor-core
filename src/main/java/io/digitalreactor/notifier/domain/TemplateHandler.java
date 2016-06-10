package io.digitalreactor.notifier.domain;

/**
 * Created by ingvard on 03.06.2016.
 */
public interface TemplateHandler<T> {
    T handel(String transport, Notification notification);
}
