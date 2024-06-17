package com.example.mqttdemo.service;

import com.example.mqttdemo.util.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandlingException;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    @Autowired
    private MessageChannel mqttOutboundChannel;

    private final String secretKey = "1234567890123456"; // 加密金鑰

    // 寄訊息
    public void sendMessage(String topic, String message) throws Exception {
        String encryptedMessage = CryptoUtil.encrypt(message, secretKey);
        try {
            mqttOutboundChannel.send(MessageBuilder.withPayload(encryptedMessage)
                    .setHeader("mqtt_topic", topic)
                    .build());
        }
        catch (MessageHandlingException e){
            System.out.println("Message handling exception"+e.getMessage());
            e.printStackTrace();
        }
    }
    // 解密
    public String decryptMessage(String encryptedMessage) throws Exception {
        return CryptoUtil.decrypt(encryptedMessage, secretKey);
    }
}