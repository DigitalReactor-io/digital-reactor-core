package io.digitalreactor.notifier.domain.adapter;

import io.digitalreactor.notifier.domain.Notification;
import io.digitalreactor.notifier.domain.NotificationTemplateMapper;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by ingvard on 03.06.2016.
 */
public abstract class AbstractSendAdapter implements SenderAdapter {

    protected NotificationTemplateMapper<MailMessage> notificationTemplateMapper;

    @Override
    public void send(String transport, Notification notification) {
        send(notificationTemplateMapper.map(transport, notification));
    }

    public abstract void send(MailMessage message);
}
