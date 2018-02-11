package com.netty.learnnetty14.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 单独写一个AcceptorIdleStateTrigger，
 * 其实也是继承ChannelInboundHandlerAdapter，
 * 重写userEventTriggered方法，因为客户端是write，
 * 那么服务端自然是read，设置的状态就是IdleState.READER_IDLE，
 * 源码如下：
 */

// Sharable注解主要是用来标示一个ChannelHandler可以被安全地共享，
// 即可以在多个Channel的ChannelPipeline中使用同一个ChannelHandler，
// 而不必每一个ChannelPipeline都重新new一个新的ChannelHandler。
// 也就是说您的ChannelHandler是线程安全的。
// 这种情况比如会用在统计整体的吞吐量的时候用到。
@ChannelHandler.Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleState state = ((IdleStateEvent)evt).state();
            if(IdleState.READER_IDLE.equals(state)){
                throw new Exception("idle exception");
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
