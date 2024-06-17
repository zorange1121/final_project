package com.example.mqttdemo.controller;

import com.example.mqttdemo.service.MqttService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class MqttController {

    @Autowired
    private MqttService mqttService;

    @GetMapping("/send")
    public String send(@RequestParam String topic, @RequestParam String message) {
        try {
            mqttService.sendMessage(topic, message);
            return "Message sent";
        } catch (Exception e) {
            return "Error sending message: " + e.getMessage();
        }
    }

    @GetMapping("/decrypt")
    public String decrypt(@RequestParam String encryptedMessage) {
        try {
            String decodedMessage = URLDecoder.decode(encryptedMessage, StandardCharsets.UTF_8.name());
            System.out.println("System_Log : Received encrypted message: " + decodedMessage);
            return mqttService.decryptMessage(decodedMessage);
        } catch (Exception e) {
            System.out.println("System_Log : Error decrypting message: " + encryptedMessage);
            e.printStackTrace();
            return "Error decrypting message: " + e.getMessage();
        }
    }
}
