package com.dzyls.chat.notify.impl;

import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:03
 * @Version 1.0.0
 * @Description:
 */
public class AsyncNotice implements Notice {

    private List<ChannelHandlerContext> contextList = new ArrayList<>();

    @Async
    @Override
    public void noticeClient(String message) {
        contextList.forEach(c ->{
            c.writeAndFlush(message);
        });
    }

}
