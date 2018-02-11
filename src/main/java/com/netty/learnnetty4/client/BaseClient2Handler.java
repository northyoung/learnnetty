package com.netty.learnnetty4.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端的第二个自定义的inbound处理器
 *
 */
public class BaseClient2Handler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("BaseClient2Handler Active");
    }
}
