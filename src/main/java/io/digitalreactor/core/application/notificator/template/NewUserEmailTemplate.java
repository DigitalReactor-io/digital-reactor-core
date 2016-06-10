package io.digitalreactor.core.application.notificator.template;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import io.digitalreactor.core.application.notificator.EmailAdapter;
import io.digitalreactor.core.application.notificator.notice.NewUserEmailNotice;
import io.digitalreactor.core.domain.notificator.Message;
import io.digitalreactor.core.domain.notificator.Notice;
import io.digitalreactor.core.domain.notificator.Template;
import io.vertx.core.json.JsonObject;

import java.io.IOException;


/**
 * Created by MStepachev on 16.05.2016.
 */
public class NewUserEmailTemplate implements Template {

    public final static String name = "NewUserEmailTemplate";

    @Override
    public String templateName() {
        return name;
    }

    @Override
    public Message handle(Notice notice) {
        NewUserEmailNotice newUserEmailNotice = (NewUserEmailNotice) notice;

        String content = "";
        try {
            TemplateLoader loader = new ClassPathTemplateLoader();
            loader.setPrefix("/src/main/resources/notificator/template");
            Handlebars handlebars = new Handlebars(loader);

            com.github.jknack.handlebars.Template template = handlebars.compile("NewUserEmail");

            content = template.apply(new JsonObject()
                    .put("username", newUserEmailNotice.getUsername())
                    .put("password", newUserEmailNotice.getPassword())
            );

        } catch (IOException e) {
            e.printStackTrace();
        }


        EmailAdapter.EmailMessage emailMessage = new EmailAdapter.EmailMessage(newUserEmailNotice.getFrom(), newUserEmailNotice.getTo(), newUserEmailNotice.getTitle(), content);

        return emailMessage;
    }

}
