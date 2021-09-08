package com.dzyls.chat.input.impl;

import com.dzyls.chat.entity.CommonRequest;
import com.dzyls.chat.input.Input;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/8 21:44
 * @Version 1.0.0
 * @Description:
 */
public class CommandLineInput implements Input{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineInput.class);

    private Scanner scanner;

    private ChannelHandlerContext context;

    public CommandLineInput(ChannelHandlerContext context) {
        this.context = context;
        scanner = new Scanner(System.in);
    }

    private Runnable producer = ()->{
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            context.writeAndFlush(CommonRequest.generateSendRequest(message));
        }
    };

    @Override
    public void inputMessage() {
        Thread producerThread = new Thread(producer,"CommandLineInput-Thread");
        producerThread.start();
        LOGGER.info("start CommandLineInput");
    }
}
