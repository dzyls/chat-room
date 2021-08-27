package com.dzyls.chat.handler;

import com.dzyls.chat.annotate.HandlerOrder;
import com.dzyls.chat.entity.CommonRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 15:22
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandlerOrder(order = 1)
@ConditionalOnProperty(prefix = "chat",name = "role",havingValue = "server")
public class IdleChannelCloseHandler extends ChannelDuplexHandler {

    /**
     * Logger for this class
     */
    private static final Logger LOG = LoggerFactory.getLogger(IdleChannelCloseHandler.class);

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
            }
            CommonRequest heartBeatMessage = CommonRequest.generateSendRequest("heart beat message");
            ctx.writeAndFlush("Hello");
            //LOG.info("Idle channel close.");
        }
    }

}
