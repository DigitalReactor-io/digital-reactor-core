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
    public final static String GET_BY_ID = BASE_USER_MANAGER + "get_by_id";


    private MongoClient client;
    private String PROJECTS_COLLECTION = "projects";

    @Override
    public void start() throws Exception {
        JsonObject mongoDbClientConfig = new JsonObject()
                .put("host", System.getenv("DB_MG_HOST"))
                .put("port", System.getenv("DB_MG_PORT"))
                .put("username", System.getenv("DB_MG_USERNAME"))
                .put("password", System.getenv("DB_MG_PASSWORD"))
                .put("db_name", System.getenv("DB_MG_NAME"));

        client = MongoClient.createShared(vertx, mongoDbClientConfig);

        vertx.eventBus().consumer(NEW_PROJECT, this::createNewProject);
        vertx.eventBus().consumer(MARK_AS_LOADING, this::markAsLoadingSummary);
        vertx.eventBus().consumer(MARK_AS_COMPLETED, this::markSummaryAsCompleted);
        vertx.eventBus().consumer(GET_BY_ID, this::getById);

    }

    private void getById(Message message) {
        int projectId = Integer.valueOf(((JsonObject)message.body()).getString("id"));

        client.find(PROJECTS_COLLECTION, new JsonObject().put("_id", projectId), res -> {
            if (res.succeeded()) {
                JsonObject summary = null;
                if(res.result().isEmpty()){
                    summary = new JsonObject();
                } else {
                    summary = res.result().get(0);
                }

                message.reply(summary);
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    private void markSummaryAsCompleted(Message message) {
        int projectId = Integer.valueOf(((JsonObject) message.body()).getString("projectId"));
        String summaryId = ((JsonObject) message.body()).getString("summaryId");
        String dateCompleted = ((JsonObject) message.body()).getString("date");
        SummaryShort summaryShort = new SummaryShort(summaryId, dateCompleted);

        client.update(
                PROJECTS_COLLECTION,
                new JsonObject().put("_id", projectId),
                new JsonObject()
                        .put("$set", new JsonObject().put("status", new JsonObject(Json.encode(new ProjectStatus(ProjectStatus.ProjectType.COMPLETED, summaryShort)))))
                        .put("$push", new JsonObject().put("history", new JsonObject(Json.encode(summaryShort)))),
                result -> {
                    if (result.failed()) {
                        message.fail(0, result.cause().getMessage());
                    }
                }
        );
    }

    private void markAsLoadingSummary(Message message) {
        int projectId = Integer.valueOf(((JsonObject) message.body()).getString("projectId"));
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
        int externalProjectId = Integer.valueOf(((JsonObject) message.body()).getString("id"));
        Project createNewProject = new Project(externalProjectId);

        client.save(PROJECTS_COLLECTION, new JsonObject(Json.encode(createNewProject)), result -> {
            if (result.failed()) {
                message.fail(0, result.cause().getMessage());
            }
        });

    }
}
