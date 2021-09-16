package com.dzyls.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/16 3:51
 * @Version 1.0.0
 * @Description:
 */
@Data
@AllArgsConstructor
public class ChatMessage {

    private String clientName;

    private String message;

    private long createTime = System.currentTimeMillis();

}
