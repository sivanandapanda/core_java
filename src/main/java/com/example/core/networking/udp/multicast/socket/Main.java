package com.example.core.networking.udp.multicast.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        String group = "230.0.0.0";

        UdpMulticastServer server = new UdpMulticastServer(group);
        UdpMulticastClient client = new UdpMulticastClient(group);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(server);
        executorService.submit(client);
    }
}
