package com.dzyls.chat.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
