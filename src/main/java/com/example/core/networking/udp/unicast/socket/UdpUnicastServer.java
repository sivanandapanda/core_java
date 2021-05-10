package com.example.core.networking.udp.unicast.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.nio.charset.StandardCharsets.UTF_8;

public class UdpUnicastServer implements Runnable {
    private final int clientPort;

    public UdpUnicastServer(int clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        try(DatagramSocket serverSocket = new DatagramSocket(50000)) {
            for (int i = 0; i < 3; i++) {
                String message = "Message number " + i;
                DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(UTF_8), message.length(), InetAddress.getLocalHost(), clientPort);
                serverSocket.send(datagramPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
