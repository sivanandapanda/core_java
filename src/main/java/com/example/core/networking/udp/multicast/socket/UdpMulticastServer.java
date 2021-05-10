package com.example.core.networking.udp.multicast.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpMulticastServer implements Runnable {
    private final String group;

    public UdpMulticastServer(String group) {
        this.group = group;
    }

    @Override
    public void run() {
        try(DatagramSocket socket = new DatagramSocket()) {
            InetAddress group = InetAddress.getByName(this.group);

            byte[] buf = "Hello from multicast server".getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
            socket.send(packet);

            buf = "end".getBytes();
            packet = new DatagramPacket(buf, buf.length, group, 4446);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
