package com.dzyls.chat.annotate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface HandlerOrder {

    public static final int LOWEST_ORDER = Integer.MAX_VALUE;

    public static final int MAX_ORDER = Integer.MIN_VALUE;

    int order() default LOWEST_ORDER;

}
