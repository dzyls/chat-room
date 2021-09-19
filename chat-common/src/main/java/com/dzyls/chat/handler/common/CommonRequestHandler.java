package com.dzyls.chat.handler.common;

import com.dzyls.chat.annotate.Client;
import com.dzyls.chat.annotate.Server;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 22:23
 * @Version 1.0.0
 * @Description: 请求处理器
 * 根据#{@link OperationType}分为不同的处理器，
 * 搭配服务器注解{@link Server} & {@link Client}
 * 可以分别被服务器或客户端加载。
 */
public interface CommonRequestHandler {

    void handle(CommonRequest commonRequest, ChannelHandlerContext ctx);

}
