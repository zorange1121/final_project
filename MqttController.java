package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private Mqttuser mqttuser = new Mqttuser();

    @RequestMapping("/MainPage")
    public String index(@RequestParam(value="title", required=false, defaultValue="") String title, Model model) {
        model.addAttribute("name", title);
        return "final";
    }

    @GetMapping("/post")
    public void post(@RequestParam String content) {
        mqttuser.publish(content);
    }

    @GetMapping("/get")
    public ResponseEntity<Object> getMsg() {
        MqttSubscriber mqttSubscriber = MqttSubscriber.getInstance();
        String msg = mqttSubscriber.getMessage();
        return ResponseEntity.ok(msg);
    }
}

