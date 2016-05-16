package io.digitalreactor.core.application.notificator.template;

import io.digitalreactor.core.domain.notificator.Template;

/**
 * Created by MStepachev on 16.05.2016.
 */
public class NewUserEmailTemplate implements Template {

    public final static String name = "NewUserEmailTemplate";

    public String render() {
        return null;
    }

    @Override
    public String templateName() {
        return name;
    }
}
