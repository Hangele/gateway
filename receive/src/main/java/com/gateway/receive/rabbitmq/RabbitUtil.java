package com.gateway.receive.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class RabbitUtil {
    @Resource
    private RabbitTemplate rabbitTemplate;

    public RabbitUtil(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendData(Object data) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, null, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
