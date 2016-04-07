package io.digitalreactor.core;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

import java.util.UUID;

/**
 * Created by ingvard on 03.04.16.
 */
public class App {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions deploymentOptions =  new DeploymentOptions().setWorker(true);

        Loader loader = new Loader();
        Processor processor = new Processor();
        WebServer restController = new WebServer();
        SummaryDispatcherVerticle summaryDispatcherVerticle = new SummaryDispatcherVerticle();

        vertx.deployVerticle(loader);
        vertx.deployVerticle(summaryDispatcherVerticle);
        vertx.deployVerticle(processor, deploymentOptions);
        vertx.deployVerticle(restController, deploymentOptions);
    }
}
