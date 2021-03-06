package com.dzyls.chat.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/8/26 22:11
 * @Version 1.0.0
 * @Description:
 */
@Data
@NoArgsConstructor
public class CommonRequest {

    private OperationType operationType;

    private String sender;

    private String message;

    private Map<String,Object> paramMap;

    public Object putParam(String key, Object value){
        if (paramMap == null){
            paramMap = new HashMap<>();
        }
        return paramMap.put(key,value);
    }

    public static CommonRequest generateSendRequest(String message){
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setOperationType(OperationType.MSG);
        commonRequest.setMessage(message);
        return commonRequest;
    }

    public static CommonRequest generateHeartBeatRequest(String message){
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setOperationType(OperationType.HEART_BEAT);
        commonRequest.setMessage(message);
        return commonRequest;
    }

    public static CommonRequest generateSendRequestWithSender(String message,String sender){
        CommonRequest request = generateSendRequest(message);
        request.setSender(sender);
        return request;
    }

    public static CommonRequest generateNameRequest(String name){
        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setOperationType(OperationType.NAME);
        commonRequest.setMessage(name);
        return commonRequest;
    }
}
