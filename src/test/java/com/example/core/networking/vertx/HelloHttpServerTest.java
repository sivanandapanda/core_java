package com.example.core.networking.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.net.ServerSocket;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HelloHttpServerTest {
    private Vertx vertx;
    private int port = 8081;

    @BeforeEach
    public void setup(VertxTestContext testContext) throws IOException {
        vertx = Vertx.vertx();

        // Pick an available and random
        ServerSocket socket = new ServerSocket(0);
        port = socket.getLocalPort();
        socket.close();

        DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject().put("http.port", port));

        vertx.deployVerticle(HelloHttpServer.class.getName(), options, testContext.succeedingThenComplete());
    }

    @AfterEach
    public void tearDown(VertxTestContext testContext) {
        assertThat(vertx.deploymentIDs())
                .isNotEmpty()
                .hasSize(1);
    }

    @Test
    public void whenReceivedResponse_thenSuccess(VertxTestContext testContext) {
        vertx.createHttpClient().request(HttpMethod.GET, port, "localhost", "/")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertThat(buffer.toString()).contains("Welcome to vertx Http Server");
                    testContext.completeNow();
                })));

        //WebClient webClient = WebClient.create(vertx);
        /*vertx.deployVerticle(new HelloHttpServer(), testContext.succeeding(id -> {
            webClient.get(11981, "localhost", "/yo")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(resp -> {
                        testContext.verify(() -> {
                            assertThat(resp.statusCode()).isEqualTo(200);
                            assertThat(resp.body()).contains("Yo!");
                            testContext.completeNow();
                        });
                    }));*/
       // }));
    }

}