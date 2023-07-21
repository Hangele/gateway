package com.gateway.parse.rabbitmq;

import com.gateway.parse.dto.RecordDataDTO;
import com.gateway.parse.handler.MessageBatchHandler;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQParseListener {

	@RabbitListener(queues = {"gateway.parse.data"})
	private void parseData(@Payload Message msg, RecordDataDTO data, Channel channel) {
		MessageBatchHandler.doDispatch(data, channel, msg.getMessageProperties().getDeliveryTag());
	}
}
