package io.digitalreactor.core.domain.notificator;

/**
 * Created by MStepachev on 16.05.2016.
 */
public interface Template {
    String templateName();

    Message handle(Notice notice);
}
