package com.dzyls.chat.client.handler;

import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/25 22:56
 * @Version 1.0.0
 * @Description:
 */
public class HeartBeatHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ByteBuf byteBuf = in;
        int readerIndex = byteBuf.readerIndex();
        int readableBytes = byteBuf.readableBytes();
        byte []bs = new byte[readableBytes];
        byteBuf.getBytes(readerIndex,bs);
        CommonRequest commonRequest = KryoUtil.kryoDeserialize(bs, CommonRequest.class);
        byteBuf.skipBytes(readableBytes);
        out.add(commonRequest);
    }

}
