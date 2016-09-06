package io.digitalreactor.core;

import io.digitalreactor.core.domain.messages.ReportMessage;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Created by ingvard on 07.04.16.
 */
public class SummaryStorageVerticle extends ReactorAbstractVerticle {

    private final static String BASE_USER_MANAGER = "digitalreactor.core.summaries.";
    public final static String NEW = BASE_USER_MANAGER + "new";
    public final static String ENRICH = BASE_USER_MANAGER + "enrich";
    public final static String GET_BY_ID = BASE_USER_MANAGER + "get_by_id";


    private final String SUMMARIES_COLLECTION = "summaries";
    private MongoClient client;

    @Override
    public void start() throws Exception {
        JsonObject mongoDbClientConfig = new JsonObject()
                .put("host", System.getenv("DB_MG_HOST"))
                .put("port", Integer.valueOf(System.getenv("DB_MG_PORT")))
                .put("username", System.getenv("DB_MG_USERNAME"))
                .put("password", System.getenv("DB_MG_PASSWORD"))
                .put("db_name", System.getenv("DB_MG_NAME"));

        client = MongoClient.createShared(vertx, mongoDbClientConfig);

        vertx.eventBus().consumer(NEW, this::createSummary);
        vertx.eventBus().consumer(ENRICH, this::enrichSummary);
        vertx.eventBus().consumer(GET_BY_ID, this::getById);
    }

    private void createSummary(Message message) {
        String summaryId = ((JsonObject) message.body()).getString("summaryId");
        client.save(SUMMARIES_COLLECTION, new JsonObject().put("_id", summaryId), result -> {
            if (result.succeeded()) {
                String id = result.result();
                message.reply(new JsonObject().put("summaryId", summaryId));
            } else {
                message.fail(0, result.cause().getMessage());
            }
        });
    }

    private void enrichSummary(Message message) {
        ReportMessage msg = toObj((String) message.body(), ReportMessage.class);
        String summaryId = msg.summaryId;
        String reportJson = msg.report;

        client.update(SUMMARIES_COLLECTION,
                new JsonObject().put("_id", summaryId),
                new JsonObject().put("$push", new JsonObject().put("reports", new JsonObject(reportJson))),
                result -> {

                }
        );
    }

    private void getById(Message message) {
        String summaryId = ((JsonObject) message.body()).getString("summaryId");

        client.find(SUMMARIES_COLLECTION, new JsonObject().put("_id", summaryId), res -> {
            if (res.succeeded()) {
                JsonObject summary = res.result().get(0);

                message.reply(summary);
            } else {
                res.cause().printStackTrace();
            }
        });
    }

}
