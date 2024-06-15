package com.example;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import org.eclipse.paho.client.mqttv3.*;

public class test3 {

    private static final String BROKER = "tcp://broker.emqx.io:1883";
    private static final String SUBSCRIBE_TOPIC = "userone";
    private static final String PUBLISH_TOPIC = "usertwo";
    private static final String USERNAME = "usertwo";
    private static final String PASSWORD = "public";
    private static final int QOS = 0;

    public static void main(String[] args) {
        new Thread(new MqttSubscriber()).start();

        publish();
    }

    public static void publish() {
        try {
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USERNAME);
            options.setPassword(PASSWORD.toCharArray());
            options.setConnectionTimeout(60);
            options.setKeepAliveInterval(60);

            client.connect(options);
            Scanner scanner = new Scanner(System.in,"Big5");
            System.out.println("請輸入");

            while (true) {
                System.out.println("Enter content to publish:");
                String content = scanner.nextLine();
                if (content.equals("-1")) {
                    break;
                }
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
            }

            client.disconnect();
            client.close();
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    static class MqttSubscriber implements Runnable {
        @Override
        public void run() {
            try {
                MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId(), new MemoryPersistence());
                MqttConnectOptions options = new MqttConnectOptions();
                options.setUserName(USERNAME);
                options.setPassword(PASSWORD.toCharArray());
                options.setConnectionTimeout(60);
                options.setKeepAliveInterval(60);

                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("Connection lost: " + cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        System.out.println("Message received");
                        System.out.println("receive topic: " + topic);
                        String apple="";
                        try {
                            apple = new String(message.getPayload(), "Big5");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        System.out.println("receive message content: " + apple);

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("deliveryComplete---------" + token.isComplete());
                    }
                });

                client.connect(options);
                client.subscribe(SUBSCRIBE_TOPIC, QOS);

                Thread.sleep(Long.MAX_VALUE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
