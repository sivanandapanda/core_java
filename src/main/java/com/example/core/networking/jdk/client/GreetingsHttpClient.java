package com.example.core.networking.jdk.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class GreetingsHttpClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        GreetingsHttpClient greetingsHttpClient = new GreetingsHttpClient(8001, "localhost");
        HttpClient httpClient = greetingsHttpClient.create();

        //todo name with space doesn't work, escape characters needs to be added manually

        HttpResponse<String> getResponse = httpClient.send(greetingsHttpClient.createGetHttpRequest("foo"), BodyHandlers.ofString());
        System.out.println("Get response status code " + getResponse.statusCode());
        System.out.println("Get response headers " + getResponse.headers());
        System.out.println("Get body " + getResponse.body());

        httpClient.sendAsync(greetingsHttpClient.createGetHttpRequest("foo"), BodyHandlers.ofString())
                .thenAccept(response -> {
                    System.out.println("Get response status code " + response.statusCode());
                    System.out.println("Get response headers " + response.headers());
                    System.out.println("Get body " + response.body());
                });

        HttpResponse<String> postResponse = httpClient.send(greetingsHttpClient.createPostHttpRequest("foo"), BodyHandlers.ofString());
        System.out.println("Post response status code " + postResponse.statusCode());
        System.out.println("Post response headers " + postResponse.headers());
        System.out.println("Post body " + postResponse.body());

        httpClient.sendAsync(greetingsHttpClient.createPostHttpRequest("foo"), BodyHandlers.ofString())
                .thenAccept(response -> {
                    System.out.println("Post response status code " + response.statusCode());
                    System.out.println("Post response headers " + response.headers());
                    System.out.println("Post body " + response.body());
                });
    }

    private final int port;
    private final String host;

    public GreetingsHttpClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    private HttpClient create() {
        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    private HttpRequest createGetHttpRequest(String name) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s/greet?name=%s", host, port, name)))
                .GET()
                .build();
    }

    private HttpRequest createPostHttpRequest(String name) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format("http://%s:%s/greet", host, port)))
                .POST(HttpRequest.BodyPublishers.ofString(name))
                .build();
    }
}
