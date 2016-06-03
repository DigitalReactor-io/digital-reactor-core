package io.digitalreactor.notifier.application.email.mapper;

import io.digitalreactor.notifier.domain.Notification;
import io.digitalreactor.notifier.domain.NotificationTemplateMapper;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by ingvard on 03.06.2016.
 */
public class NotificationTemplateEmailMapper extends NotificationTemplateMapper<MailMessage> {
    @Override
    public MailMessage map(String transport, Notification notification) {
        return templateHandlers.get(notification.eventType()).handel(transport, notification);
    }
}
