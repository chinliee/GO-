package com.team4.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMenuOrderFinishedMessage(String destination, Object message) {
        // SimpMessagingTemplate convertAndSend 方法
        // 第一個參數，WebSocket 要發送到前端的目標路徑，也就是前端前端訂閱的頻道(端點)路徑
        // 舉例前端程式碼：this.stompClient.subscribe('/topic/greetings', function(response) {})
        // 代表訂閱 /topic/greetings 頻道端點url，所以這邊第一個參數就要寫 "/topic/greetings"，這樣前端才能收到後端發送的 WebSocket 資料

        // 第二個參數，是要發送的 WebSocket 通知、訊息資料，會自動把物件轉成 JSON 傳到前端
        messagingTemplate.convertAndSend(destination, message);
    }

}
