package com.netty.learnnetty12.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    private static final ByteBuf HEARTBEAT_SEQUENCE =
            Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

    private static final Integer TRY_TIMES = 2;

    private int currentTime = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("激活时间是"+ new Date());
        System.out.println("HeartBeatClientHandler channelActive");
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("停止时间是"+new Date());
        System.out.println("HeartBeatClientHandler channelInactive");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        System.out.println("循环触发时间："+new Date());
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if(event.state() == IdleState.WRITER_IDLE){
                if(currentTime <= TRY_TIMES){
                    System.out.println("currentTime"+currentTime);
                    currentTime++;
                    ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg){
        String message = (String)msg;
        System.out.println(message);
        if(message.equals("Heartbeat")){
            ctx.write("has read message from server");
            ctx.flush();
        }
        ReferenceCountUtil.release(message);
    }
}
