package com.example.core.networking.vertx.rest;

import com.example.core.networking.vertx.rest.model.Article;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class HelloRestServer extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloRestServer());
    }

    @Override
    public void start(Promise<Void> startPromise) {
        Router router = Router.router(vertx);
        router.route(HttpMethod.GET, "/api/article/:id")
                .handler(this::handleGet);

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(config().getInteger("http.port", 8003),
                        result -> {
                            if(result.succeeded()) {
                                startPromise.complete();
                            } else {
                                startPromise.fail(result.cause());
                            }
                        });
    }

    private void handleGet(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        Article article = new Article(id, "Atomic Habits", "James Clear", "2019-05-20");

        routingContext.response()
                .putHeader("content/type", "application/json")
                .setStatusCode(200)
                .end(Json.encodePrettily(article));
    }
}
