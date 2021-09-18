package com.dzyls.chat.handler.common.impl;

import com.dzyls.chat.annotate.Client;
import com.dzyls.chat.annotate.HandleType;
import com.dzyls.chat.contants.ChatConstants;
import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.entity.OperationType;
import com.dzyls.chat.handler.common.CommonRequestHandler;
import com.dzyls.chat.input.impl.CommandLineInput;
import com.dzyls.chat.notify.Notice;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Scanner;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/16 21:49
 * @Version 1.0.0
 * @Description:
 */
@Client
@Component
@HandleType(type = OperationType.NAME)
public class ClientNameRequestHandler implements CommonRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClientNameRequestHandler.class);

    @Resource
    private Notice syncNotice;

    @Override
    public void handle(CommonRequest commonRequest, ChannelHandlerContext ctx) {
        if (ChatConstants.loginSuccess.equals(commonRequest.getMessage())){
            logger.info("login success.");
            new CommandLineInput(syncNotice).inputMessage();
            return;
        }
        System.out.printf("请输入用户名 : ");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        ctx.writeAndFlush(CommonRequest.generateNameRequest(userName));
    }

}
