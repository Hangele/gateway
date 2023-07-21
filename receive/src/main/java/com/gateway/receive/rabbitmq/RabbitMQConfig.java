package com.gateway.receive.rabbitmq;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Author alen
 * @DATE 2022/6/7 23:50
 */
@Slf4j
@Configuration
public class RabbitMQConfig {
 
    public static final String EXCHANGE_NAME = "gateway_exchange";
    public static final String GATEWAY_ORIGIN_DATA = "gateway.origin.data";
    public static final String GATEWAY_PRASE_DATA = "gateway.parse.data";
    public static final String GATEWAY_TRANS_DATA = "gateway.trans.data";

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        //开启Mandatory,触发回调函数
        rabbitTemplate.setMandatory(true);
        //ack
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("-----------confirm begin---------------");
            log.info("data:" + correlationData);
            if(ack){
                log.info("Ack:true");
            }else{
                log.info("Ack:false");
            }
            log.info("cause:" + cause);
            log.info("-----------confirm end---------------");
        });
        //return
        rabbitTemplate.setReturnsCallback(returnedMessage -> {
            log.info("-----------return begin---------------");
            log.info("message:"+returnedMessage.getMessage());
            log.info("reply code:"+returnedMessage.getReplyCode());
            log.info("reply text:"+returnedMessage.getReplyText());
            log.info("exchange:"+returnedMessage.getExchange());
            log.info("routeKey:"+returnedMessage.getRoutingKey());
            log.info("-----------return end---------------");
        });

        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 1.
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange gatewayExchange() {
        /**
         * FanoutExchange的参数说明:
         * 1. 交换机名称
         * 2. 是否持久化 true：持久化，交换机一直保留 false：不持久化，用完就删除
         * 3. 是否自动删除 false：不自动删除 true：自动删除
         */
        return new FanoutExchange(EXCHANGE_NAME, true, false);
    }
 
    /**
     * 2.
     * 声明队列
     * @return
     */
    @Bean
    public Queue originQueue() {
        /**
         * Queue构造函数参数说明
         * 1. 队列名
         * 2. 是否持久化 true：持久化 false：不持久化
         */
        return new Queue(GATEWAY_ORIGIN_DATA, true);
    }
 
    @Bean
    public Queue parseQueue() {
        return new Queue(GATEWAY_PRASE_DATA, true);
    }
 
    @Bean
    public Queue transQueue() {
        return new Queue(GATEWAY_TRANS_DATA, true);
    }
 
    /**
     * 3.
     * 队列与交换机绑定
     */
    @Bean
    public Binding smsBinding() {
        return BindingBuilder.bind(originQueue()).to(gatewayExchange());
    }
 
    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(parseQueue()).to(gatewayExchange());
    }
 
    @Bean
    public Binding wechatBinding() {
        return BindingBuilder.bind(transQueue()).to(gatewayExchange());
    }
}