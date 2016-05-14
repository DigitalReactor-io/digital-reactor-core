package io.digitalreactor.core;

import io.digitalreactor.core.domain.model.notice.Notice;
import io.digitalreactor.core.domain.model.notice.NoticeAddress;
import io.digitalreactor.core.domain.model.notice.NoticeContent;
import io.digitalreactor.core.domain.model.notice.NoticeMethod;
import io.vertx.core.eventbus.Message;

/**
 * Created by ingvard on 14.05.16.
 */
public class NotificatorVerticle extends ReactorAbstractVerticle {

    private final static String BASE_USER_MANAGER = "digitalreactor.core.notificator.";
    public final static String NOTIFY = BASE_USER_MANAGER + "notify";


    @Override
    public void start() throws Exception {

        vertx.eventBus().consumer(NOTIFY, this::notifier);
    }

    private void notifier(Message message) {
        //TODO[St.maxim] correct type convert by jackson
        Notice notice = (Notice) message.body();

        for (NoticeMethod method : notice.getMethods()) {
            targetNotify(notice.getAddress(), method, notice.getContent());
        }

    }

    private void targetNotify(NoticeAddress address, NoticeMethod method, NoticeContent content) {
        //TODO[St.maxim] strategy resolver
    }

}
