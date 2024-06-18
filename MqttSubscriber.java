package com.example.demo;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.*;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicReference;

public class MqttSubscriber implements MqttCallback {
    private static final String BROKER = "tcp://broker.emqx.io:1883";
    private static final String SUBSCRIBE_TOPIC = "userfour";
    private static final String USERNAME = "userthree";
    private static final String PASSWORD = "public";
    private static final int QOS = 0;

    private static MqttSubscriber instance;
    private final AtomicReference<String> apple = new AtomicReference<>("");

    private MqttSubscriber() {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            client.setCallback(this);
            client.connect(options);
            client.subscribe(SUBSCRIBE_TOPIC, QOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static MqttSubscriber getInstance() {
        if (instance == null) {
            synchronized (MqttSubscriber.class) {
                if (instance == null) {
                    instance = new MqttSubscriber();
                }
            }
        }
        return instance;
    }

    public String getMessage() {
        return apple.getAndSet("");
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost: " + cause.getMessage());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Message received");
        System.out.println("receive topic: " + topic);
        try {
            String payload = new String(message.getPayload(), "Big5");
            apple.set(payload);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("receive message content: " + apple.get());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }
}
