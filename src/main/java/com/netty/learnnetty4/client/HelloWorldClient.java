package com.netty.learnnetty4.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloWorldClient {
    private static final String HOST = System.getProperty("host","127.0.0.1");
    private static final Integer PORT = Integer.parseInt(System.getProperty("port","8080"));
    private static final Integer SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder",new StringDecoder());
                            p.addLast("encoder",new StringEncoder());
                            p.addLast(new BaseClient1Handler());
                            p.addLast(new BaseClient2Handler());
                        }
                    });
            ChannelFuture future = b.connect(HOST,PORT).sync();
            future.channel().writeAndFlush("Hello Netty server ,I am a common client");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
