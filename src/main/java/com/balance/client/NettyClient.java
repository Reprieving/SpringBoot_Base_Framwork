package com.balance.client;

import com.balance.architecture.dto.TransferObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClient {
    private static final Logger logger = LoggerFactory
            .getLogger(NettyClient.class);
    public static void connect(int port, String host, final TransferObject transferObject){
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)),
                                    new NettyClientHandler(transferObject));
                        }
                    });

            ChannelFuture f = b.connect(host, port).sync().addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    logger.error("Connect to "+ host +" error: ");
                    transferObject.setMessage("Connect to host error: " + future.cause());
                }
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Netty 's thread sync error");
            transferObject.setState(TransferObject.STATUS_FAIL);
            transferObject.setMessage("Netty 's thread sync error");
        } finally {
            group.shutdownGracefully();
        }
    }


    public static TransferObject fileAccess(TransferObject transferObject){
        int port = 9999;
        String host = "localhost";
        connect(port,host,transferObject);

        return transferObject;
    }
}
