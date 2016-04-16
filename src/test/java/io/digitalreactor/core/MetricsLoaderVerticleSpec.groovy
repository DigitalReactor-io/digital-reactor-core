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

    //TODO[st.maxim] naming
    def "happy path "() {
        setup:
        metricsLoaderVerticle.vertx = Mock(Vertx.class)

        EventBus eventBus = Mock(EventBus.class)
        metricsLoaderVerticle.vertx.eventBus() >> eventBus

        HttpClient httpClient = Mock(HttpClient.class)
        metricsLoaderVerticle.vertx.createHttpClient() >> httpClient

        eventBus.consumer(_, _ as Handler<Message>) >> { address, handler ->
            eventBusHandlers.put(address, handler)
        }
        metricsLoaderVerticle.start();
        Message message = Mock(Message.class)
        when:
        eventBusHandlers.get(MetricsLoaderVerticle.LOAD_REPORT).handle(message)
        then:
        1 * httpClient.get(_, _, _, _)
        //then:
        //metricsLoaderVerticle.loadReport


    }
}
