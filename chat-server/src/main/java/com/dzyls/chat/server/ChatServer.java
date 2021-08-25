package com.dzyls.chat.server;

import com.dzyls.chat.server.handler.IdleChannelCloseHandler;
import com.dzyls.chat.server.handler.MessageDecoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 14:32
 * @Version 1.0.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "server")
public class ChatServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatServer.class);

    private int port = 8080;

    private int workThreadCount = 16;

    private boolean useEpoll = false;

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    private int backlog = 1024;

    private int idleTime = 10000;

    @PostConstruct
    public void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        startServer();
        stopWatch.stop();
        LOGGER.info("start server success , port : {} , elapsed time: {}ms", port, stopWatch.getTotalTimeMillis());
    }

    @PreDestroy
    public void destory() {
        shutdownGracefully(boss);
        shutdownGracefully(worker);
        LOGGER.info("close chat server");
    }

    private void shutdownGracefully(EventLoopGroup group) {
        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
        }
    }


    public void startServer() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        Class<? extends ServerChannel> channelClz = initEventLoopGroup();
        serverBootstrap.group(boss, worker)
                .channel(channelClz)
                // Nagle off
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, false)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, backlog)

                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        addCodecs(pipeline);
                        addIdleHandler(pipeline);
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            LOGGER.error("Chat Server was Interrupted. ", e);
        }
    }

    private void addCodecs(ChannelPipeline pipeline){
        pipeline.addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(1 << 20,0,4,0,4));
        pipeline.addLast("lengthEncoder",new LengthFieldPrepender(4));
        pipeline.addLast("messageDecoder",new MessageDecoder());
    }

    private void addIdleHandler(ChannelPipeline pipeline) {
        IdleStateHandler idleStateHandler = new IdleStateHandler(0, 0, idleTime, TimeUnit.MILLISECONDS);
        ChannelDuplexHandler idleChannelCloseHandler = new IdleChannelCloseHandler();
        pipeline.addLast("idleStateHandler", idleStateHandler);
        pipeline.addLast("idleChannelCloseHandler", idleChannelCloseHandler);
    }

    /**
     * init event Loop Group
     * if os is not support epoll, will use NioEventLoopGroup instead.
     * @return the class of ServerChannel
     */
    private Class<? extends ServerChannel> initEventLoopGroup() {
        Class<? extends ServerChannel> channelClz = null;
        boolean useEpollSucc = false;
        if (useEpoll) {
            try {
                boss = new EpollEventLoopGroup(1);
                worker = new EpollEventLoopGroup(Runtime.getRuntime().availableProcessors() << 1);
                channelClz = EpollServerSocketChannel.class;
                LOGGER.info("use epoll to handle");
                useEpollSucc = true;
            } catch (UnsatisfiedLinkError e) {
                LOGGER.warn("Os not support epoll");
            }
        }
        if (!useEpollSucc) {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            channelClz = NioServerSocketChannel.class;
            LOGGER.info("use NioEventLoopGroup to handle");
        }
        return channelClz;
    }

}
