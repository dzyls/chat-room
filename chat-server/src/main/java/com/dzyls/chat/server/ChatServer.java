package com.dzyls.chat.server;

import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.handler.common.codec.CommonRequestCodec;
import com.dzyls.chat.notify.Notice;
import com.dzyls.chat.util.HandlerOrderComparator;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 14:32
 * @Version 1.0.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "server")
@Log4j2
public class ChatServer implements ApplicationContextAware {

    private int port = 8080;

    private int workThreadCount = 16;

    private boolean useEpoll = false;

    private EventLoopGroup boss;

    private EventLoopGroup worker;

    private int backlog = 1024;

    private int idleTime = 10000;

    private ApplicationContext applicationContext;

    @Resource
    private ChatContext chatContext;

    @Resource
    private Notice asyncNotice;

    @Resource
    private List<ChannelHandler> channelHandlerList;

    @PostConstruct
    public void init() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        startServer();
        channelHandlerList.sort(HandlerOrderComparator.INSTANCE);
        stopWatch.stop();
        log.info("start server success , port : {} , elapsed time: {}ms", port, stopWatch.getTotalTimeMillis());
    }

    @PreDestroy
    public void destroy() {
        shutdownGracefully(boss);
        shutdownGracefully(worker);
        log.info("close chat server");
    }

    private void shutdownGracefully(EventLoopGroup group) {
        if (group != null && !group.isShutdown()) {
            group.shutdownGracefully();
        }
    }

    /**
     * start chat server
     */
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
                        addChannelHandler(pipeline);
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
        } catch (InterruptedException e) {
            log.error("Chat Server was Interrupted. ", e);
        }
    }

    /**
     * add common channel handler
     * @param pipeline
     */
    private void addCodecs(ChannelPipeline pipeline){
        pipeline.addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(1 << 20,0,4,0,4));
        pipeline.addLast("lengthEncoder",new LengthFieldPrepender(4));
        pipeline.addLast("messageDecoder",new CommonRequestCodec());
    }

    /**
     * add custom channel handler
     * if custom channel handler is not {@link io.netty.channel.ChannelHandler.Sharable}
     * will create new bean by reflection
     * @param pipeline
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void addChannelHandler(ChannelPipeline pipeline) throws InstantiationException, IllegalAccessException {
        for (ChannelHandler channelHandler : channelHandlerList) {
            Class<? extends ChannelHandler> handlerClass = channelHandler.getClass();
            ChannelHandler.Sharable sharable = handlerClass.getAnnotation(ChannelHandler.Sharable.class);
            if (sharable == null){
                pipeline.addLast(handlerClass.getSimpleName(),handlerClass.newInstance());
                continue;
            }
            pipeline.addLast(handlerClass.getSimpleName(), channelHandler);
        }
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
                log.info("use epoll to handle");
                useEpollSucc = true;
            } catch (UnsatisfiedLinkError e) {
                log.warn("Os not support epoll");
            }
        }
        if (!useEpollSucc) {
            boss = new NioEventLoopGroup();
            worker = new NioEventLoopGroup();
            channelClz = NioServerSocketChannel.class;
            log.info("use NioEventLoopGroup to handle");
        }
        return channelClz;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
