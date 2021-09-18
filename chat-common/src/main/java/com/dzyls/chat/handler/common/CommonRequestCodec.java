package com.dzyls.chat.handler.common;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.contants.ChatAttributeKey;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
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
public class CommonRequestCodec extends ByteToMessageCodec<CommonRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CommonRequest msg, ByteBuf out) throws Exception {
        byte[] bytes = KryoUtil.kryoSerialize(msg);
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int readerIndex = byteBuf.readerIndex();
        int readableBytes = byteBuf.readableBytes();
        byte []bs = new byte[readableBytes];
        byteBuf.getBytes(readerIndex,bs);
        CommonRequest commonRequest = KryoUtil.kryoDeserialize(bs, CommonRequest.class);
        byteBuf.skipBytes(readableBytes);
        String clientName = String.valueOf(channelHandlerContext.channel().attr(ChatAttributeKey.CLIENT_NAME_KEY).get());
        commonRequest.setSender(clientName);
        list.add(commonRequest);
    }

}
