package com.dzyls.chat.util;

import com.dzyls.chat.annotate.HandlerOrder;
import io.netty.channel.ChannelHandler;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 23:09
 * @Version 1.0.0
 * @Description:
 */
public class HandlerOrderUtil {

    public static final int getOrder(ChannelHandler channelHandler){
        HandlerOrder order = channelHandler.getClass().getAnnotation(HandlerOrder.class);
        return order == null ? HandlerOrder.HANDLER_ORDER : order.order();
    }

}
