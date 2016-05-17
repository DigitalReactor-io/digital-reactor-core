package io.digitalreactor.core.application.notificator.notice;

import io.digitalreactor.core.application.notificator.EmailAdapter;
import io.digitalreactor.core.application.notificator.template.NewUserEmailTemplate;
import io.digitalreactor.core.domain.notificator.Email;
import io.digitalreactor.core.domain.notificator.Message;
import io.digitalreactor.core.domain.notificator.Notice;

/**
 * Created by MStepachev on 16.05.2016.
 */
public class NewUserEmailNotice implements Email, Notice {

    private final String template = NewUserEmailTemplate.name;
    private final String transport = EmailAdapter.transportName;

    private final String username;
    private final String password;
    private final String title;
    private final String to;

    public NewUserEmailNotice(String username, String password, String title, String to) {
        this.username = username;
        this.password = password;
        this.title = title;
        this.to = to;
    }

    @Override
    public String getFrom() {
        return null;
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
        return null;
    }

    @Override
    public String templateName() {
        return template;
    }

    //TODO[St.maxim] что-то не то, ту не должно быть этого метода
    @Override
    public Message handle(Notice notice) {
        return null;
    }


    @Override
    public String transport() {
        return transport;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
