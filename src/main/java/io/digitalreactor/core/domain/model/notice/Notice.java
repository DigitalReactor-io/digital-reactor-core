package io.digitalreactor.core.domain.model.notice;

import java.util.List;

/**
 * Created by ingvard on 14.05.16.
 */
public class Notice {
    private List<NoticeMethod> methods;
    private NoticeAddress address;
    private NoticeContent content;

    public List<NoticeMethod> getMethods() {
        return methods;
    }

    public NoticeAddress getAddress() {
        return address;
    }

    public NoticeContent getContent() {
        return content;
    }
}
