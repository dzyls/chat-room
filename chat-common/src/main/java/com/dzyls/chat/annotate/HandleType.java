package com.dzyls.chat.annotate;

import com.dzyls.chat.entity.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface HandleType {

    OperationType type() default OperationType.LOGIN;

}
