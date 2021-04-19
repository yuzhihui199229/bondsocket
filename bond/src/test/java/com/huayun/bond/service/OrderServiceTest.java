package com.huayun.bond.service;

import com.huayun.bond.pojo.MessageProtocol;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void qryOrder() {
        MessageProtocol msg = new MessageProtocol();
        MessageProtocol messageProtocol = null;
        try {
            messageProtocol = orderService.qryOrder(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(messageProtocol.toString());
    }
}