package io.digitalreactor.notifier.domain;

import java.util.List;

/**
 * Created by ingvard on 03.06.2016.
 */
public interface NotificationRouter {
    List<Route> findRoutesByEvent(String eventName);
}
