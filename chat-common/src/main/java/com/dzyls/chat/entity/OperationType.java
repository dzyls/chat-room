package com.dzyls.chat.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/22 15:53
 * @Version 1.0.0
 * @Description:
 */
public enum OperationType {

    LOGIN(1),
    MSG(2),
    LOGOUT(3),
    HEART_BEAT(4),
    NAME(5);

    private final int index;

    private static volatile Map<Integer,OperationType> operationTypeMap;

    private OperationType(int index) {
        this.index = index;
    }

    public static OperationType getOperationType(int index){
        if (operationTypeMap == null){
            synchronized (OperationType.class){
                if (operationTypeMap == null){
                    operationTypeMap = new HashMap<>();
                    for (OperationType value : OperationType.values()) {
                        operationTypeMap.put(value.index,value);
                    }
                }
            }
        }
        return operationTypeMap.get(index);
    }
}
