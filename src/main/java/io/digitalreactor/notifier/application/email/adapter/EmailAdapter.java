package io.digitalreactor.notifier.application.email.adapter;

import io.digitalreactor.notifier.domain.NotificationTemplateMapper;
import io.digitalreactor.notifier.domain.adapter.AbstractSendAdapter;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by ingvard on 03.06.2016.
 */
public class EmailAdapter extends AbstractSendAdapter {
    private final String ADAPTER_NAME = "email";
    private final MailClient mailClient;

    public EmailAdapter(MailClient mailClient, NotificationTemplateMapper<MailMessage> notificationTemplateMapper) {
        this.mailClient = mailClient;
        this.notificationTemplateMapper = notificationTemplateMapper;
    }

    @Override
    public String name() {
        return ADAPTER_NAME;
    }

    @Override
    public void send(MailMessage message) {
        //TODO[St.maxim] logger
        mailClient.sendMail(message, result -> {
            if (result.succeeded()) {
                System.out.println(result.result());
            } else {
                result.cause().printStackTrace();
            }
        });
    }
}
