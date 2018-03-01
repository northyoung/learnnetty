package com.netty.learnnetty15.client;

import com.netty.learnnetty15.attributeMap.AttributeMapConstant;
import com.netty.learnnetty15.attributeMap.NettyChannel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;

import java.util.Date;


public class HelloWorld2ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx){
//        Attribute<NettyChannel> attr = ctx.attr(AttributeMapConstant.NETTY_CHANNEL_KEY);
        //测试Channel上的AttributeMap
        Attribute<NettyChannel> attr = ctx.channel().attr(AttributeMapConstant.NETTY_CHANNEL_KEY);
        NettyChannel nChannel = attr.get();
        if(nChannel == null){
            NettyChannel newNChannel = new NettyChannel("HelloWorld2Client", new Date());
            nChannel = attr.setIfAbsent(newNChannel);
        } else {
            System.out.println("channelActive attributeMap 中是有值的");
            System.out.println(nChannel.getName()+"====="+nChannel.getCreateDate());
        }
        System.out.println("HelloWorld2ClientHandler Active");
        ctx.fireChannelActive();
    }

    public void channelRead(ChannelHandlerContext ctx,Object msg){
//        Attribute<NettyChannel> attr = ctx.attr(AttributeMapConstant.NETTY_CHANNEL_KEY);
        //测试Channel上的AttributeMap
        Attribute<NettyChannel> attr = ctx.channel().attr(AttributeMapConstant.NETTY_CHANNEL_KEY);
        NettyChannel nChannel = attr.get();
        if(nChannel == null){
            NettyChannel newNChannel = new NettyChannel("HelloWorld0client",new Date());
            nChannel = attr.setIfAbsent(newNChannel);
        } else {
            System.out.println("channelRead attributeMap 中有值");
            System.out.println(nChannel.getName() + "=======" + nChannel.getCreateDate());
        }
        System.out.println("HelloWorldClientHandler read Message:" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
