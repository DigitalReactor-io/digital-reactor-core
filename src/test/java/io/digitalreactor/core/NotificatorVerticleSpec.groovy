package io.digitalreactor.core

import io.digitalreactor.core.application.notificator.EmailAdapter
import io.digitalreactor.core.application.notificator.notice.NewUserEmailNotice
import io.digitalreactor.core.application.notificator.template.NewUserEmailTemplate
import io.digitalreactor.core.domain.notificator.MessageBuilder
import io.digitalreactor.core.domain.notificator.Sender
import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.Message
import io.vertx.ext.mail.MailClient
import io.vertx.ext.mail.MailResult
import spock.lang.Specification
/**
 * Created by MStepachev on 17.05.2016.
 */
public class NotificatorVerticleSpec extends Specification {

    def "happy path"() {
        setup:
        NotificatorVerticle notificatorVerticle = new NotificatorVerticle();
        NewUserEmailNotice newUserEmailNotice = new NewUserEmailNotice('username', 'password', 'title', 'to');
        Message message = Mock(Message.class);
        message.body() >> newUserEmailNotice;
        MailClient mailClient = Mock(MailClient.class);
        notificatorVerticle.sender = new Sender();
        notificatorVerticle.sender.registerSendHandler(new EmailAdapter(mailClient));
        notificatorVerticle.messageBuilder = new MessageBuilder();
        notificatorVerticle.messageBuilder.registerTemplateHandler(new NewUserEmailTemplate());
        when:
        notificatorVerticle.notifier(message);
        then:
        1 * mailClient.sendMail(_, _ as Handler<AsyncResult<MailResult>>) >> { email, handle ->

        }
    }

}