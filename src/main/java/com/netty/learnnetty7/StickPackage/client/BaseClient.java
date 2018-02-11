package com.netty.learnnetty7.StickPackage.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

public class BaseClient {
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
                        public void initChannel(SocketChannel ch){
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new StringDecoder());
                            p.addLast(new BaseClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(HOST,PORT).sync();
            future.channel().writeAndFlush("Hello Netty server,I am a common client");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
