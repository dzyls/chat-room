package com.dzyls.chat.handler.impl;

import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.CommonRequestHandler;
import io.netty.channel.ChannelHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 22:29
 * @Version 1.0.0
 * @Description:
 */
@Component
@HandleType(type = OperationType.MSG)
@ChannelHandler.Sharable
public class MsgRequestHandler implements CommonRequestHandler {
    @Override
    public void handle(CommonRequest commonRequest) {
        Assert.notNull(commonRequest,"CommonRequest is null");
        System.out.println(commonRequest.getMessage());
    }
}
