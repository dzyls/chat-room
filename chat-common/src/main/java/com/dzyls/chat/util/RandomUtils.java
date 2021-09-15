package com.dzyls.chat.util;

import java.util.UUID;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/15 21:49
 * @Version 1.0.0
 * @Description:
 */
public final class RandomUtils {

    public static String uuid(){
        return UUID.randomUUID().toString();
    }

}
