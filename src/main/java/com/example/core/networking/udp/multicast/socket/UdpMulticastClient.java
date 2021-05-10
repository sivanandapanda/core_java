package com.example.core.networking.udp.multicast.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UdpMulticastClient implements Runnable {
    private final String group;

    public UdpMulticastClient(String group) {
        this.group = group;
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        try(MulticastSocket socket = new MulticastSocket(4446)) {
            InetAddress group = InetAddress.getByName(this.group);
            socket.joinGroup(group);
            while (true) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);

                if ("end".equals(received)) {
                    break;
                }
            }
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
