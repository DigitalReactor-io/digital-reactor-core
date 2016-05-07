package io.digitalreactor.core

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.http.HttpClient
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by ingvard on 12.04.16.
 */
class MetricsLoaderVerticleSpec extends Specification {

    @Shared MetricsLoaderVerticle metricsLoaderVerticle
    @Shared Map<String, Handler<Message>> eventBusHandlers

    def setupSpec() {
        metricsLoaderVerticle = new MetricsLoaderVerticle()
        eventBusHandlers = new HashMap<>()
    }
}
