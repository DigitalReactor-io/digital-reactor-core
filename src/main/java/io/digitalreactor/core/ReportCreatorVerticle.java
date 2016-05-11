package io.digitalreactor.core;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.vertx.core.eventbus.EventBus;

/**
 * Created by ingvard on 07.04.16.
 */
public class ReportCreatorVerticle extends ReactorAbstractVerticle {

    public static final String CREATE_REPORT = "report.creator.create_report";

    @Override
    public void start() throws Exception {
        EventBus eventBus = vertx.eventBus();

        eventBus.consumer(CREATE_REPORT, msg -> {
            ReportMessage message = toObj((String) msg.body(), ReportMessage.class);

            //todo map query result to report and serialize to json

            eventBus.publish(SummaryDispatcherVerticle.ENRICH_SUMMARY, fromObj(message));
        });

    }
}
