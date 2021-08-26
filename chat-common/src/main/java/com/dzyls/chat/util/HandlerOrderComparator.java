package com.dzyls.chat.util;

import io.netty.channel.ChannelHandler;

import java.util.Comparator;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 23:10
 * @Version 1.0.0
 * @Description:
 */
public class HandlerOrderComparator implements Comparator<ChannelHandler> {

    public static final HandlerOrderComparator INSTANCE = new HandlerOrderComparator();

    @Override
    public int compare(ChannelHandler o1, ChannelHandler o2) {
        return compare0(o1,o2);
    }

    private int compare0(ChannelHandler o1, ChannelHandler o2){
        int i1 = HandlerOrderUtil.getOrder(o1);
        int i2 = HandlerOrderUtil.getOrder(o2);
        return Integer.compare(i1, i2);
    }

}
