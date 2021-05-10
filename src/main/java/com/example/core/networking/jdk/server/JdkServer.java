package com.example.core.networking.jdk.server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * curl http://localhost:8001/greet?name=abc
 * curl -d "post" -X POST http://localhost:8001/greet
 */
public class JdkServer {

    private final int port;

    public JdkServer(int port) {
        this.port = port;
    }

    private void start() throws IOException {
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        server.createContext("/greet", new GreetingsHttpHandler());
        server.setExecutor(threadPoolExecutor);
        server.start();
        System.out.println(" Server started on port 8001");
    }

    public static void main(String[] args) throws IOException {
        new JdkServer(8001).start();
    }

}
