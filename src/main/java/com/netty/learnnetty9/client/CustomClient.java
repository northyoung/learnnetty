package com.netty.learnnetty9.client;

import com.netty.learnnetty9.decoder.client.CustomEncoder;
import com.netty.learnnetty9.decoder.server.CustomDecoder;
import com.netty.learnnetty9.server.CustomServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class CustomClient {
    private static final String HOST = System.getProperty("host","127.0.0.1");
    private static final Integer PORT = Integer.parseInt(System.getProperty("port","8080"));
    private static final Integer SIZE = Integer.parseInt(System.getProperty("size","256"));

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CustomEncoder());
                            ch.pipeline().addLast(new CustomClientHandler());
                        }
                    });
            ChannelFuture future = b.connect(HOST,PORT).sync();
            future.channel().writeAndFlush("Hello Netty Server ,I am a common client");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            group.shutdownGracefully();
        }
    }
}
