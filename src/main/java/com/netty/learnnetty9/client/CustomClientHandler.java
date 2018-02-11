package com.netty.learnnetty9.client;

import com.netty.learnnetty9.CustomMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CustomClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        CustomMsg customMsg = new CustomMsg((byte)0xAB,(byte)0XCD,"Hello,Netty".length(),"Hello,Netty");
        ctx.writeAndFlush(customMsg);
    }
}
