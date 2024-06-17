package com.example.demo;

import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import org.eclipse.paho.client.mqttv3.*;

public  class MqttSubscriber implements Runnable {
    private  String BROKER = "tcp://broker.emqx.io:1883";
    private  String SUBSCRIBE_TOPIC = "userfour";
    private  String PUBLISH_TOPIC = "userthree";
    private  String USERNAME = "userthree";
    private  String PASSWORD = "public";
    private  int QOS = 0;
    String apple="";

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
                    apple="";
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
    public String geString(){
        return apple;
    }
    public void setString(){
        apple="";
    }
}
