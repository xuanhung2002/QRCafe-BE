package com.qrcafe.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
  @MessageMapping("/process-data")
  @SendTo("/topic/processed-data")
  public void processInputData(String string) {
    // Process the inputData and return the result
    System.out.println(string);
  }

}
