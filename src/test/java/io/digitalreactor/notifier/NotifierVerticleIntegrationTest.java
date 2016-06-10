package io.digitalreactor.notifier;

import io.digitalreactor.notifier.application.email.adapter.EmailAdapter;
import io.digitalreactor.notifier.application.email.mapper.NotificationTemplateEmailMapper;
import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by MStepachev on 08.06.2016.
 */
@RunWith(VertxUnitRunner.class)
public class NotifierVerticleIntegrationTest {

    private Vertx vertx;
    private NotifierVerticle notifierVerticle;

    @Before
    public void setUp(TestContext context) throws IOException {

        vertx = Vertx.vertx();
        //TODO deploy options

        NotificationTemplateEmailMapper notificationTemplateEmailMapper = new NotificationTemplateEmailMapper();
        notificationTemplateEmailMapper.registerHandler();

        EmailAdapter emailAdapter = new EmailAdapter(mailClient(), notificationTemplateEmailMapper);
        notifier.registerAdapter(emailAdapter);

        notifierVerticle = new NotifierVerticle();


        vertx.deployVerticle(notifierVerticle, context.asyncAssertSuccess());
    }



    @After
    public void tearDown(TestContext context) {
        vertx.close(context.asyncAssertSuccess());
    }

    @Test
    public void sendEmailMessage_NewUserEvent_EmailTemplateSend(TestContext context) {

        vertx.eventBus().send(NotifierVerticle.Address.NOTIFY.address(), null);

    }

}