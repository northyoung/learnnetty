package com.netty.learnnetty13.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 *  客户端心跳代码增加重连机制
 */
public class HeartBeatsClient {
    public void connect(int port , String host) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture future = null;
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("ping",new IdleStateHandler(0,4,0, TimeUnit.SECONDS));
                            ch.pipeline().addLast("decoder", new StringDecoder());
                            ch.pipeline().addLast("encode",new StringEncoder());
                            ch.pipeline().addLast(new HeartBeatClientHandler());
                        }
                    });
             future = b.connect(host,port).sync();
            future.channel().closeFuture().sync();
        } finally {
//            group.shutdownGracefully();
            //重连机制
            if(null != future){
                if(future.channel()!=null && future.channel().isOpen()){
                    future.channel().close();
                }
            }
            System.out.println("准备重连");
            connect(port,host);
            System.out.println("重连成功");
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
            }
        }
        new HeartBeatsClient().connect(port, "127.0.0.1");
    }

}
