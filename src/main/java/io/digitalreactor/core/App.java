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

        WebServer restController = new WebServer();
        SummaryDispatcherVerticle summaryDispatcherVerticle = new SummaryDispatcherVerticle();
        UserManagerVerticle userManagerVerticle = new UserManagerVerticle();

        vertx.deployVerticle(summaryDispatcherVerticle);
        vertx.deployVerticle(userManagerVerticle);
        vertx.deployVerticle(restController, deploymentOptions);
    }
}
