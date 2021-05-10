package com.example.core.networking.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class HelloHttpServer extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HelloHttpServer());
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer()
        .requestHandler(r -> r.response().end("Welcome to vertx Http Server"))
        .listen(config().getInteger("http.port", 8002),
                result -> {
                    if(result.succeeded()) {
                        startPromise.complete();
                    } else {
                        startPromise.fail(result.cause());
                    }
                });
    }
}
