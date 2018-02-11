package com.netty.learnnetty4.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 客户端的第一个自定义的inbound处理器
 */
public class BaseClient1Handler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("BaseClient1Handler channelAcrive");
        //来触发调用下一个handler中的ChannelActive方法(BaseClient2Handler)
        //ctx.fireChannelActive();
    }

    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("BaseClient1Handler  channelInactive ");
    }
}
