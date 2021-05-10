package com.example.core.networking.udp.unicast.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpUnicastClient implements Runnable {
    private final int port;

    public UdpUnicastClient(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try(DatagramSocket clientSocket = new DatagramSocket(port)) {
            byte[] buffer = new byte[65507];
            clientSocket.setSoTimeout(3000);

            while(true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, 0, buffer.length);
                clientSocket.receive(datagramPacket);

                String receivedMessage = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
