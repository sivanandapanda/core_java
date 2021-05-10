package com.example.core.networking.udp.multicast.channel;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

public class UdpMulticastClient implements Runnable {
    private final String group;

    public UdpMulticastClient(String group) {
        this.group = group;
    }

    @Override
    public void run() {
        try {
            NetworkInterface ni = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
            InetAddress group = InetAddress.getByName(this.group);

            DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET)
                    .setOption(StandardSocketOptions.SO_REUSEADDR, true)
                    .bind(new InetSocketAddress(5000))
                    .setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
            MembershipKey key = dc.join(group, ni);

            ByteBuffer byteBuffer = ByteBuffer.allocate(1500);
            while (true) {
                if (key.isValid()) {
                    byteBuffer.clear();
                    InetSocketAddress sa = (InetSocketAddress) dc.receive(byteBuffer);
                    //byteBuffer.flip();

                    System.out.println("Multicast received from " + sa.getHostString());

                    String s = new String(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.position());

                    System.out.println(s);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}