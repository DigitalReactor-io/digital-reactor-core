package io.digitalreactor.core.application.Service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mail.MailClient;
import io.vertx.ext.mail.MailConfig;
import io.vertx.ext.mail.MailMessage;
import org.apache.commons.collections.map.HashedMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ingvard on 16.07.16.
 * TODO[St.maxim] It is not production code, only dev
 */
public class EmailService {

    private final MailClient mailClient;
    private final String sendersAddress;
    private Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(Vertx vertx, String sendersAddress) {
        this.sendersAddress = sendersAddress;
        this.mailClient = mailClient(vertx);
    }

    public void send(String sendTo, String subject, String htmlMessages) {

        MailMessage mailMessage = new MailMessage();
        mailMessage.setFrom(sendersAddress);
        mailMessage.setTo(sendTo);
        mailMessage.setSubject(subject);
        mailMessage.setHtml(htmlMessages);

        mailClient.sendMail(mailMessage, result -> {
            if (result.succeeded()) {
                logger.debug("Message sent.");
            } else {
                logger.error("Error when message was sent.", result.cause());
            }
        });
    }

    //TODO[St.maxim] go away SOLID principle :) just a fast coding
    public void newUserMessage(String email, String password) {

        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/template");
        Handlebars handlebars = new Handlebars(loader);
        try {
            com.github.jknack.handlebars.Template template = handlebars.compile("welcome");
            Map<String, String> templateParam = new HashMap<>();
            templateParam.put("email", email);
            templateParam.put("password", password);
            String content = template.apply(templateParam);

            send(email, "Регистрация нового пользователя", content);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Template not found", e);
        }


    }

    private MailClient mailClient(Vertx vertx) {

        MailConfig config = new MailConfig();
        config.setHostname(System.getenv("MAIL_SERVER"));
        config.setPort(Integer.valueOf(System.getenv("MAIL_POST")));
        config.setSsl(Boolean.valueOf(System.getenv("MAIL_SSL")));
        config.setUsername(System.getenv("MAIL_USERNAME"));
        config.setPassword(System.getenv("MAIL_PASSWORD"));

        return MailClient.createNonShared(vertx, config);
    }
}
