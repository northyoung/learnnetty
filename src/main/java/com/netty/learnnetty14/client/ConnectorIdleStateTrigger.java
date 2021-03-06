package com.netty.learnnetty14.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class ConnectorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    public static final ByteBuf HEART_SEQUENCE = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent) evt).state();
            if(state == IdleState.WRITER_IDLE){
                //write heartbeat to server
                ctx.writeAndFlush(HEART_SEQUENCE.duplicate());
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
