package com.gateway.parse.handler;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import com.gateway.parse.base.IParse;
import com.gateway.parse.dto.RecordDataDTO;
import com.gateway.parse.entity.DataBody;
import com.gateway.parse.entity.EvGBProtocol;
import com.gateway.parse.utils.DataUtil;
import com.gateway.parse.utils.DateUtil;
import com.rabbitmq.client.Channel;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 描述: 消息多线程同时处理
 * @Author: chenhb2
 * @Date: 2023-4-23 14:14
 */
@Slf4j
public class MessageBatchHandler {

    public static ThreadPoolExecutor pool = new ThreadPoolExecutor(50, 100, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder().setNamePrefix("ParsePool-").build(),
            new ThreadPoolExecutor.AbortPolicy());

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> pool.shutdown()));
    }

    public static void doDispatch(RecordDataDTO data, Channel channel, long deliveryTag) {
        pool.execute(new MessageHandleTask(data, channel, deliveryTag));
    }

    private static class MessageHandleTask implements Runnable {
        RecordDataDTO message;
        Channel channel;
        long deliveryTag;
        private EvGBProtocol producer = new EvGBProtocol();

        public MessageHandleTask(RecordDataDTO message, Channel channel, long deliveryTag) {
            this.message = message;
            this.channel = channel;
            this.deliveryTag = deliveryTag;
        }

        @Override
        public void run() {
            log.info("开始解析VIN：{}，数据网关时间：{}，数据：{}", this.message.getVin(), this.message.getGwTime(), this.message.getData());
            try {
                byte[] bytes = DataUtil.getBytesByHexString(this.message.getData());
                ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
                EvGBProtocol protocol = producer.decode(byteBuf);
                DataBody body = protocol.getBody();
                if(body!=null && protocol.getLength()>0){
                    JSONObject json = SpringUtil.getBean(IParse.class).parseUpJson(protocol.getCommandType(), body.getByteBuf());
                    body.setJson(json);
                }
                protocol.setBody(body);
                // TODO 保存数据 可存入mysql hbase等
                String gwTime = DateUtil.getFormatStrByLong(protocol.getGatewayReceiveTime());
                if (null != protocol.getCommandType()) {
                    switch (protocol.getCommandType().getId()) {
                        case 1:
                            // TODO 记录 车辆登入 信息
                            break;
                        case 2:
                            // TODO 记录 实时数据 信息
                            break;
                        case 3:
                            // TODO 记录 补发数据 信息
                            break;
                        case 4:
                            // TODO 记录 车辆登出 信息
                            break;
                        default:
                            break;
                    }
                }
                // 手动确认消息，若自动确认则不需要写以下该行
                channel.basicAck(deliveryTag, false);
            } catch (IOException e) {
                System.err.println("fail to confirm message:" + message);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


}

