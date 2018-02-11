package com.netty.learnnetty9.server;

import com.netty.learnnetty9.CustomMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CustomServerHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,Object msg){
        if(msg instanceof CustomMsg){
            CustomMsg customMsg = (CustomMsg)msg;
            System.out.println("Client->server:"+ctx.channel().remoteAddress()+"send"+customMsg.getBody());
        }
    }
}
