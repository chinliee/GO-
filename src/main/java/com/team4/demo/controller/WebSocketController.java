package com.team4.demo.controller;

import com.team4.demo.model.dto.websocket.WebSocketReceivedDto;
import com.team4.demo.model.dto.websocket.WebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // @MessageMapping，類似 @RequestMapping 的功能，前端發送 WebSocket 請求 -> 指定後端對應處理的 url 路徑
    // @MessageMapping，對應前端程式碼：this.stompClient.send('/sendMsgToUser/1', {}, JSON.stringify(param1))
    // @DestinationVariable 和 @PathVariable 功能類似，是 WebSocket 請求取得 url 路徑參數的標註
    @MessageMapping("/sendMsgToUser/{userId}")
    public void sendMsgToUser(@DestinationVariable("userId") Long userId, WebSocketReceivedDto dto) {
        log.info("WebSocketController - sendMsgToUser ... userId => {}, dto => {}", userId, dto);
        WebSocketMessage message = new WebSocketMessage("一對一 WebSocket 回傳回應 => " + dto.getContent());
        messagingTemplate.convertAndSend("/member/" + userId + "/message", message);
    }

    // 這個寫法，和下面的寫法，效果完全相同
    // 差別這裡是用 SimpMessagingTemplate 的 convertAndSend() 方法，指定要發送到前端的目標路徑、和要發送的資料
    // 下面則是用標註 @SendTo，指定要發送到前端的目標路徑，並在方法上指定要傳送的資料型別
    @MessageMapping("/hello")
    public void greeting1(WebSocketReceivedDto dto){
        log.info("WebSocketController - greeting ... dto => {}", dto);
        WebSocketMessage message = new WebSocketMessage("WebSocket 回傳回應 => " + dto.getContent());
        messagingTemplate.convertAndSend("/topic/greetings", message);
    }

    // @SendTo，後端發送 WebSocket 資料到前端的目標路徑，也就是前端頁面如果使用 Stomp.js 訂閱的 WebSocket 頻道
    // 前端就要訂閱此路徑，才能接收到後端傳送的 WebSocket 資料
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public WebSocketMessage greeting2(WebSocketReceivedDto dto){
//        log.info("WebSocketController - greeting ... dto => {}", dto);
//        WebSocketMessage message = new WebSocketMessage("WebSocket 回傳回應 => " + dto.getContent());
//        return message;
//    }

}
