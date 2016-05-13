package io.digitalreactor.core;

import io.digitalreactor.core.domain.model.Project;
import io.digitalreactor.core.domain.model.ProjectStatus;
import io.digitalreactor.core.domain.model.SummaryShort;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by MStepachev on 13.05.2016.
 */
public class ProjectManagerVerticle extends AbstractVerticle {

    private final static String BASE_USER_MANAGER = "digitalreactor.core.project.";
    public final static String NEW_PROJECT = BASE_USER_MANAGER + "new";
    public final static String MARK_AS_COMPLETED = BASE_USER_MANAGER + "completed";
    public final static String MARK_AS_LOADING = BASE_USER_MANAGER + "loading";

    private MongoClient client;
    private String PROJECTS_COLLECTION = "projects";

    @Override
    public void start() throws Exception {
        JsonObject mongoDbClientConfig = new JsonObject()
                .put("host", "ds041693.mlab.com")
                .put("port", 41693)
                .put("username", "dev-test")
                .put("password", "sdf7njDAD82")
                .put("db_name", "dev-digitalreactor");

        client = MongoClient.createShared(vertx, mongoDbClientConfig);

        vertx.eventBus().consumer(NEW_PROJECT, this::createNewProject);
        vertx.eventBus().consumer(MARK_AS_LOADING, this::markAsLoadingSummary);
        vertx.eventBus().consumer(MARK_AS_COMPLETED, this::markSummaryAsCompleted);

    }

    private void markSummaryAsCompleted(Message message) {
        int projectId = ((JsonObject) message.body()).getInteger("projectId");
        String summaryId = ((JsonObject) message.body()).getString("summaryId");
        String dateCompleted = ((JsonObject) message.body()).getString("date");
        SummaryShort summaryShort = new SummaryShort(summaryId, dateCompleted);

        client.update(
                PROJECTS_COLLECTION,
                new JsonObject().put("_id", projectId),
                new JsonObject()
                        .put("$set", new JsonObject().put("status", Json.encode(new ProjectStatus(ProjectStatus.ProjectType.COMPLETED, summaryShort))))
                        .put("$push", new JsonObject().put("history", summaryShort)),
                result -> {
                    if (result.failed()) {
                        message.fail(0, result.cause().getMessage());
                    }
                }
        );
    }

    private void markAsLoadingSummary(Message message) {
        int projectId = ((JsonObject) message.body()).getInteger("projectId");
        String summaryId = ((JsonObject) message.body()).getString("summaryId");

        client.update(
                PROJECTS_COLLECTION,
                new JsonObject().put("_id", projectId),
                new JsonObject().put("$set", new JsonObject().put("status", Json.encode(new ProjectStatus(ProjectStatus.ProjectType.LOADING, new SummaryShort(summaryId))))),
                result -> {
                    if (result.failed()) {
                        message.fail(0, result.cause().getMessage());
                    }
                }
        );
    }

    private void createNewProject(Message message) {
        int externalProjectId = ((JsonObject) message.body()).getInteger("id");
        Project createNewProject = new Project(externalProjectId);

        client.save(PROJECTS_COLLECTION, new JsonObject(Json.encode(createNewProject)), result -> {
            if (result.failed()) {
                message.fail(0, result.cause().getMessage());
            }
        });

    }
}
