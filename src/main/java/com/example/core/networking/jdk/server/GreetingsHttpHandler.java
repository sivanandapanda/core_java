package com.example.core.networking.jdk.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GreetingsHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestParamValue = null;
        if("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(exchange);
        } else if("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            requestParamValue = handlePostRequest(exchange);
        }
        handleRequest(exchange, requestParamValue);
    }

    private void handleRequest(HttpExchange exchange, String requestParamValue) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        htmlBuilder.append("<html><body>")
                .append("<h1>Hello ").append(requestParamValue).append("</h1>")
                .append("</body></html>");

        exchange.sendResponseHeaders(200, htmlBuilder.length());
        outputStream.write(htmlBuilder.toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        String[] split = httpExchange.getRequestURI().toString().split("\\?");
        if(split.length > 1) {
            String[] query = split[1].split("=");
            return query.length > 1 ? query[1] : "Good Morning";
        } else {
            return "Good Morning";
        }
    }

    private String handlePostRequest(HttpExchange httpExchange) {
        InputStream inputStream = httpExchange.getRequestBody();
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textBuilder.toString();
    }

}
