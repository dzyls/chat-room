package com.dzyls.chat.handler.client;

import com.dzyls.chat.annotate.Client;
import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 15:22
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = HandlerOrder.SYS_HANDLER_ORDER)
@Client
@Log4j2
public class IdleChannelCloseHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                //ctx.close();
            }
            else if (e.state() == IdleState.WRITER_IDLE) {
                //ctx.close();
            }
            else if (e.state() == IdleState.ALL_IDLE) {
                //ctx.close();
                CommonRequest heartBeatMessage = CommonRequest.generateHeartBeatRequest("heart beat message");
                ctx.writeAndFlush(heartBeatMessage);
            }
            //LOG.info("Idle channel close.");
        }
    }

}
