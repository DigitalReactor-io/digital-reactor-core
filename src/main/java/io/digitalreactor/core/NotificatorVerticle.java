package io.digitalreactor.core;

import io.digitalreactor.core.application.notificator.EmailAdapter;
import io.digitalreactor.core.application.notificator.template.NewUserEmailTemplate;
import io.digitalreactor.core.domain.notificator.MessageBuilder;
import io.digitalreactor.core.domain.notificator.Notice;
import io.digitalreactor.core.domain.notificator.Sender;
import io.digitalreactor.core.domain.notificator.SenderAdapter;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.StartTLSOptions;

/**
 * Created by ingvard on 14.05.16.
 */
public class NotificatorVerticle extends ReactorAbstractVerticle {

    private final static String BASE_USER_MANAGER = "digitalreactor.core.notificator.";
    public final static String NOTIFY = BASE_USER_MANAGER + "notify";

    private Sender sender;
    private MessageBuilder messageBuilder;

    @Override
    public void start() throws Exception {
        messageBuilder = new MessageBuilder();
        messageBuilder.registerTemplateHandler(new NewUserEmailTemplate());

        sender = new Sender();
        sender.registerSendHandler(emailAdapter());

        vertx.eventBus().consumer(NOTIFY, this::notifier);
    }

    public void notifier(Message message) {
        //TODO[St.maxim] correct type convert by jackson
        Notice notice = (Notice) message.body();
        sender.handle(messageBuilder.build(notice));
    }

    private SenderAdapter emailAdapter() {
        MailConfig config = new MailConfig();
        config.setHostname("mail.example.com");
        config.setPort(587);
        config.setStarttls(StartTLSOptions.REQUIRED);
        config.setUsername("user");
        config.setPassword("password");
        MailClient mailClient = MailClient.createNonShared(vertx, config);

        return new EmailAdapter(mailClient);
    }
}
