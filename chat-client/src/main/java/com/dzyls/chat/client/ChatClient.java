package com.dzyls.chat.client;

import com.dzyls.chat.client.handler.HeartBeatHandler;
import com.dzyls.chat.context.ChatContext;
import com.dzyls.chat.notify.Notice;
import com.dzyls.chat.util.HandlerOrderComparator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/25 22:22
 * @Version 1.0.0
 * @Description:
 */
@Component
@ConfigurationProperties(prefix = "client")
@Log4j2
public class ChatClient {

    @Value("${server.port:8080}")
    private int port;

    private EventLoopGroup eventLoopGroup;

    private static volatile boolean stop = false;

    private static Thread monitorThread;

    @Resource
    private List<ChannelHandler> channelHandlerList;

    @Resource
    private ChatContext chatContext;

    @Resource
    private Notice syncNotice;

    private int idleTime = 10000;

    @PostConstruct
    public void init(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        channelHandlerList.sort(HandlerOrderComparator.INSTANCE);
        startMonitorThread();
        stopWatch.stop();
        log.info("start chat client success , server port : {} , elapsed time: {}ms", port, stopWatch.getTotalTimeMillis());
    }

    @PreDestroy
    public void destroy(){
        stop = true;
        monitorThread.interrupt();
    }

    public void startClient(){
        eventLoopGroup = new NioEventLoopGroup();

        try {
            // Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast("lengthDecoder",new LengthFieldBasedFrameDecoder(1 << 20,0,4,0,4));
                            pipeline.addLast("lengthEncoder",new LengthFieldPrepender(4));
                            IdleStateHandler idleStateHandler = new IdleStateHandler(0, 0, idleTime, TimeUnit.MILLISECONDS);
                            pipeline.addLast("idleStateHandler",idleStateHandler);
                            pipeline.addLast("messageDecoder",new HeartBeatHandler());
                            addChannelHandler(pipeline);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8080).sync();
            log.info("chat client is ready...");
            // ??????????????????
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("chat client was interrupted , ",e);
        } finally {
            log.info("close chat client...");
            eventLoopGroup.shutdownGracefully();
        }
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

    public void startMonitorThread(){
        monitorThread = new Thread(()->{
            startClient();
            while (!stop){
                try {
                    TimeUnit.SECONDS.sleep(10L);
                } catch (InterruptedException e) {
                    // ignore it
                }
            }
        });
        monitorThread.start();
    }


}
