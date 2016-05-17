package io.digitalreactor.core.domain.notificator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MStepachev on 16.05.2016.
 */
public class MessageBuilder {

    private Map<String, Template> handlers = new HashMap<>();

    public void registerTemplateHandler(Template template) {
        handlers.put(template.templateName(), template);
    }

    public Message build(Notice notice) {
        Template templateHandler = handlers.get(notice.templateName());

        return templateHandler.handle(notice);
    };
}
