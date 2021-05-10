package com.example.core.networking.http2;

import com.example.core.networking.netty.http2.Http2Util;
import com.example.core.networking.netty.http2.client.Http2ClientInitializer;
import com.example.core.networking.netty.http2.client.Http2ClientResponseHandler;
import com.example.core.networking.netty.http2.client.Http2SettingsHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.ssl.SslContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Ensure the server class - Http2Server.java is already started before running this test
public class Http2ClientLiveTest {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8443;
    private SslContext sslCtx;
    private Channel channel;

    @BeforeEach
    public void setup() throws Exception {
        sslCtx = Http2Util.createSSLContext(false);
    }

    @Test
    public void whenRequestSent_thenHelloWorldReceived() throws Exception {

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        Http2ClientInitializer initializer = new Http2ClientInitializer(sslCtx, Integer.MAX_VALUE, HOST, PORT);

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.remoteAddress(HOST, PORT);
            b.handler(initializer);

            channel = b.connect()
                    .syncUninterruptibly()
                    .channel();

            System.out.println("Connected to [" + HOST + ':' + PORT + ']');

            Http2SettingsHandler http2SettingsHandler = initializer.getSettingsHandler();
            http2SettingsHandler.awaitSettings(60, TimeUnit.SECONDS);

            System.out.println("Sending request(s)...");

            FullHttpRequest request = Http2Util.createGetRequest(HOST, PORT);

            Http2ClientResponseHandler responseHandler = initializer.getResponseHandler();
            int streamId = 3;

            responseHandler.put(streamId, channel.write(request), channel.newPromise());
            channel.flush();
            String response = responseHandler.awaitResponses(60, TimeUnit.SECONDS);

            assertEquals("Hello World", response);

            System.out.println("Finished HTTP/2 request(s)");

        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    @AfterEach
    public void cleanup() {
        channel.close()
                .syncUninterruptibly();
    }
}