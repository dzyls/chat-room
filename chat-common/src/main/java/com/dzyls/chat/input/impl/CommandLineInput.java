package com.dzyls.chat.input.impl;

import com.dzyls.chat.input.Input;
import io.netty.channel.ChannelHandlerContext;

import java.util.Scanner;

/**
 * @Author <a href="stringnotnull@gmail.com">dzyls</a>
 * @Date 2021/9/8 21:44
 * @Version 1.0.0
 * @Description:
 */
public class CommandLineInput implements Input{

    private Scanner scanner;

    private ChannelHandlerContext context;

    public CommandLineInput(ChannelHandlerContext context) {
        this.context = context;
        scanner = new Scanner(System.in);
    }

    @Override
    public void inputMessage() {
        while (scanner.hasNextLine()) {
            String message = scanner.nextLine();
            context.writeAndFlush(message);
        }
    }
}
