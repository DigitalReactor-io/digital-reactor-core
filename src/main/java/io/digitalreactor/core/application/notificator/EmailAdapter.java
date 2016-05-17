package io.digitalreactor.core.application.notificator;

import io.digitalreactor.core.domain.notificator.Email;
import io.digitalreactor.core.domain.notificator.Message;
import io.digitalreactor.core.domain.notificator.SenderAdapter;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailMessage;

/**
 * Created by MStepachev on 16.05.2016.
 */
public class EmailAdapter implements SenderAdapter {

    public final static String transportName = "email";
    private MailClient mailClient;

    public EmailAdapter(MailClient mailClient) {
        this.mailClient = mailClient;
    }

    @Override
    public void send(Message message) {
        MailMessage mailMessage = mapper((EmailMessage) message);

        mailClient.sendMail(mailMessage, result -> {
            if (result.succeeded()) {
                System.out.println(result.result());
            } else {
                result.cause().printStackTrace();
            }
        });
    }

    @Override
    public String transport() {
        return transportName;
    }

    public static class EmailMessage implements Message, Email {
        private final String from;
        private final String to;
        private final String title;
        private final String content;

        public EmailMessage(String from, String to, String title, String content) {
            this.from = from;
            this.to = to;
            this.title = title;
            this.content = content;
        }

        @Override
        public String transport() {
            return transportName;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public String getContent() {
            return content;
        }
    }

    //TODO[St.maxim] MailAttachment and list to
    private MailMessage mapper(EmailMessage message) {
        MailMessage mailMessage = new MailMessage();
        mailMessage.setFrom(message.getFrom());
        mailMessage.setTo(message.getTo());
        mailMessage.setSubject(message.title);
        mailMessage.setHtml(message.content);

        return mailMessage;
    }
}
