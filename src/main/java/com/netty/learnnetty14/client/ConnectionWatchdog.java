package com.netty.learnnetty14.client;

import com.netty.learnnetty14.idle.ChannelHandlerHolder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.util.Timeout;
import io.netty.util.Timer;
import io.netty.util.TimerTask;

import java.util.concurrent.TimeUnit;

/**
 * 接下来就是重点，我们需要写一个类，
 * 这个类可以去观察链路是否断了，如果断了，
 * 进行循环的断线重连操作，ConnectionWatchdog，
 * 顾名思义，链路检测狗，我们先看完整代码：
 */


/**
 *
 * 重连检测狗，当发现当前的链路不稳定关闭之后，进行12次重连
 */

@ChannelHandler.Sharable
public abstract class ConnectionWatchdog extends ChannelInboundHandlerAdapter implements TimerTask,ChannelHandlerHolder{

    //bootstrap对象，重连的时候依旧需要这个对象
    private final Bootstrap bootstrap;
    private final Timer timer;
    private final int port;
    private final String host;
    private volatile boolean reconnect = true;
    //重连次数
    private int attempts;

    public ConnectionWatchdog(Bootstrap bootstrap, Timer timer, int port, String host, boolean reconnect) {
        this.bootstrap = bootstrap;
        this.timer = timer;
        this.port = port;
        this.host = host;
        this.reconnect = reconnect;
    }

    /**
     * channel链路每次active的时候，将其连接的次数重新☞ 0
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("当前链路已经激活了，重连尝试次数重置为0");
        attempts = 0;
        ctx.fireChannelActive();
    }

    /**
     * 当链路断开的时候会触发channelInactive这个方法，也就说触发重连的导火索是从这边开始的
     * @param ctx
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx){
        System.out.println("连接关闭");
        if(reconnect){
            System.out.println("连接关闭，进行重连");
            if(attempts < 12 ){
                attempts++;
                //重连的间隔时间会越来越长
                int timeout = 2 <<attempts;
                timer.newTimeout(this,timeout, TimeUnit.MILLISECONDS);
            }
        }
        ctx.fireChannelActive();
    }

    public void run(Timeout timeout){
        ChannelFuture future;
        //bootstrap已经初始化好了，只需要将handler填入就可以了
        synchronized (bootstrap){
            bootstrap.handler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel ch) throws Exception {
                    ch.pipeline().addLast(handlers());
                }
            });
            future = bootstrap.connect(host,port);
        }
        //future对象
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                boolean succeed = future.isSuccess();
                //如果重连失败，则调用ChannelInactive方法，再次出发重连时间，
                //一直尝试12次。
                if(!succeed){
                    System.out.println("重连失败");
                    future.channel().pipeline().fireChannelInactive();
                } else {
                    System.out.println("重连成功");
                }
            }
        });
    }
}
