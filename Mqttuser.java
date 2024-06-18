package com.example.demo;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.*;

public class Mqttuser {
    private  String BROKER = "tcp://broker.emqx.io:1883";
    private  String SUBSCRIBE_TOPIC = "usertwo";
    private  String PUBLISH_TOPIC = "userthree";
    private  String USERNAME = "userthree";
    private  String PASSWORD = "public";
    private  int QOS = 0;

    public void publish(String content) {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            client.connect(options);

            System.out.println("Enter content to publish:");
            try {
                MqttMessage message = new MqttMessage(content.getBytes("Big5"));
                message.setQos(QOS);
                client.publish(PUBLISH_TOPIC, message);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("Message published");
            System.out.println("publish topic: " + PUBLISH_TOPIC);
            System.out.println("publish message content: " + content);
            //System.out.println(content);
            

            client.disconnect();
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }
}
