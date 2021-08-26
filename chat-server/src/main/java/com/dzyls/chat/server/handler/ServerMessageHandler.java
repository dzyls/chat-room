package com.dzyls.chat.server.handler;

import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 22:39
 * @Version 1.0.0
 * @Description:
 */
@Component
public class ServerMessageHandler extends SimpleChannelInboundHandler<CommonRequest> {

    @Resource
    private List<RequestHandler> requestHandlerList;

    public Map<OperationType,RequestHandler> handlerMap;

    @PostConstruct
    public void init(){
        handlerMap = new HashMap<>();
        for (RequestHandler handler : requestHandlerList) {
            HandleType handleType = handler.getClass().getAnnotation(HandleType.class);
            handlerMap.put(handleType.type(),handler);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommonRequest msg) throws Exception {
        handlerMap.get(msg.getOperationType()).handle(msg);
    }

}
