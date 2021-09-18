package com.dzyls.chat.handler.common;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/8 22:28
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = HandlerOrder.LOWEST_ORDER)
public class StringToMessageEncoder extends MessageToMessageEncoder<String> {

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(CommonRequest.generateSendRequest(msg));
    }

}
