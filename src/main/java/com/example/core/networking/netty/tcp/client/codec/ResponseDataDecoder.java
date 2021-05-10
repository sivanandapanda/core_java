package com.example.core.networking.netty.tcp.client.codec;

import com.example.core.networking.netty.tcp.model.ResponseData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ResponseDataDecoder
  extends ReplayingDecoder<ResponseData> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        ResponseData data = new ResponseData();
        data.setIntValue(in.readInt());
        out.add(data);
    }
}