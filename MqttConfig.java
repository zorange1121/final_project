package com.example.subscribe.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class MqttConfig {

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{"tcp://localhost:1883"}); // MQTT Broker URI
        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter("mqttSubscriberClient2", mqttClientFactory(), "test");
        adapter.setOutputChannel(mqttInputChannel());
        adapter.setConverter(mqttMessageConverter());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler(RestTemplate restTemplate) {
        return message -> {
            // 接收訂閱主題的原始訊息
            String encryptedMessage = message.getPayload().toString();
            System.out.println("Received encrypted message: " + encryptedMessage);
            // 呼叫API解密 解密訊息:decryptedMessage
            String decryptedMessage = restTemplate.getForObject(
                    "http://localhost:8080/decrypt?encryptedMessage=" + encryptedMessage, String.class);

            System.out.println("Decrypted message: " + decryptedMessage);
        };
    }

    @Bean
    public MqttMessageConverter mqttMessageConverter() {
        return new DefaultPahoMessageConverter();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
