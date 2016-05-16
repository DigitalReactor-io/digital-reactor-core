package io.digitalreactor.core.domain.notificator;

/**
 * Created by MStepachev on 16.05.2016.
 */
public interface Email {
    public String getFrom();

    public String getTo();

    public String getTitle();

    public String getContent();

}
