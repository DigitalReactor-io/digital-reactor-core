package io.digitalreactor.core.domain.notificator;

/**
 * Created by MStepachev on 16.05.2016.
 */
public interface SenderAdapter extends Transport {
    void send(Message message);
}
