package com.dzyls.chat.annotate;

import com.dzyls.chat.entity.OperationType;

public @interface HandleType {

    OperationType type() default OperationType.LOGIN;

}
