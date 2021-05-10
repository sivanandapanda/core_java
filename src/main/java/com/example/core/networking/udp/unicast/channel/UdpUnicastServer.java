package com.example.core.networking.udp.unicast.channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class UdpUnicastServer implements Runnable {
    private final int clientPort;

    public UdpUnicastServer(int clientPort) {
        this.clientPort = clientPort;
    }

    @Override
    public void run() {
        try(DatagramChannel channel = DatagramChannel.open()) {
            channel.socket().bind(new InetSocketAddress(50002));

            for (int i = 0; i < 3; i++) {
                String message = "Message number " + i;

                ByteBuffer buf = ByteBuffer.allocate(100);
                buf.clear();
                buf.put(message.getBytes());
                buf.flip();

                int sentLength = channel.send(buf, new InetSocketAddress("localhost", clientPort));
                //System.out.println("sent message with length " + sentLength);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
