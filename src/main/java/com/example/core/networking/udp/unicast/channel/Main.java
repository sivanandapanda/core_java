package com.example.core.networking.udp.unicast.channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        int port = 50003;

        UdpUnicastServer server = new UdpUnicastServer(port);
        UdpUnicastClient client = new UdpUnicastClient(port);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(server);
        executorService.submit(client);
    }
}
