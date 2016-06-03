package io.digitalreactor.notifier.application.email.template;

import io.digitalreactor.notifier.domain.Notification;
import io.digitalreactor.notifier.domain.TemplateHandler;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by MStepachev on 03.06.2016.
 */
public class SummaryWasCreatedEmailTemplate implements TemplateHandler<MailMessage> {
    @Override
    public MailMessage handel(String transport, Notification notification) {
        return null;
    }
}
