package io.digitalreactor.core;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

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
        SummaryStorageVerticle summeryStorageVerticle = new SummaryStorageVerticle();
        ProjectManagerVerticle projectManagerVerticle = new ProjectManagerVerticle();
        MetricsLoaderVerticle metricsLoaderVerticle = new MetricsLoaderVerticle();
        ReportCreatorVerticle reportCreatorVerticle = new ReportCreatorVerticle();

        vertx.deployVerticle(projectManagerVerticle);
        vertx.deployVerticle(summeryStorageVerticle);
        vertx.deployVerticle(summaryDispatcherVerticle);
        vertx.deployVerticle(metricsLoaderVerticle);
        vertx.deployVerticle(reportCreatorVerticle);
        vertx.deployVerticle(userManagerVerticle);
        vertx.deployVerticle(restController, deploymentOptions);
    }
}
