package com.dzyls.chat.server.handler;

import com.dzyls.chat.annotate.HandlerOrder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 15:38
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = 2)
public class MessageDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
    }

}
