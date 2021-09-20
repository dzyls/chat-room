package com.dzyls.chat.annotate;

import com.dzyls.chat.util.HandlerOrderComparator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于指定Handler的加载顺序，
 * 搭配 {@link HandlerOrderComparator} 对被HandlerOrder标注的类进行加载
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface HandlerOrder {

    /**
     * for read & write message producer
     */
    int LOWEST_ORDER = 1 << 10;

    /**
     * for codec
     */
    int CODEC_ORDER = 1;

    int HIGH_ORDER = 1 << 1;

    int MID_ORDER = 1 << 5;

    int LOW_ORDER = 1 << 9;

    int order() default LOWEST_ORDER;

}
