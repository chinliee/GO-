package com.team4.demo.model.dto.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebSocketMessage {

    private String type;

    private Object content;

    public WebSocketMessage(Object content) {
        this.content = content;
    }

}
