package com.example.core.networking.vertx.rest;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(VertxExtension.class)
public class HelloRestServerTest {

    private int port = 8081;

    @BeforeEach
    public void setup(Vertx vertx, VertxTestContext testContext) throws IOException, InterruptedException {
        // Pick an available and random
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        vertx.createHttpServer()
                .requestHandler(req -> req.response().end())
                .listen(port)
                .onComplete(testContext.succeedingThenComplete());

        assertTrue(testContext.awaitCompletion(5, TimeUnit.SECONDS));
    }

    @AfterEach
    public void tearDown(Vertx vertx, VertxTestContext testContext) {
        assertThat(vertx.deploymentIDs())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void givenId_whenReceivedArticle_thenSuccess(Vertx vertx, VertxTestContext testContext) {
        /*final Async async = testContext.async();

        vertx.createHttpClient()
                .getNow(port, "localhost", "/api/article/12", response -> response.handler(responseBody -> {
                    testContext.assertTrue(responseBody.toString()
                            .contains("\"id\" : \"12\""));
                    async.complete();
                }));*/

        vertx.createHttpClient().request(HttpMethod.GET, port, "localhost", "/api/article/12")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    System.out.println("Response ===>" + buffer.toString());
                    assertThat(buffer.toString()).contains("\"id\" : \"12\"");
                    testContext.completeNow();
                })));
    }
}