# 32960协议接入及解析网关

## receive:
* 数据接入网关，基于spring boot、netty、RabbitMQ开发的简易协议接入网关，提供协议数据接入存储分发（基于RabbitMQ）功能

## parse:
* 数据解析网关，基于Spring boot、RabbitMQ开发的32960协议解析网关
* 解析后数据的存储未做，可根据自身需求扩展（MySQL/MongoDB/Hbase/elk等等）
* 此解析网关借鉴了一位大佬（署名dyy）上传至CSDN的解析源码，没有找到对应的github开源库（不过贴一个很类似的库：https://api.gitee.com/mybtc_0/ev-gb-gateway）
* 如有侵权请联系删除

## 使用方式
* 修改配置文件，然后正常Spring boot打包发布即可
* 接入/解析网关都可以多应用部署，增强接入解析能力（如接入可以Nginx负载均衡，解析基于RabbitMQ部署多个消费者即可）
