package com.team4.demo.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private Integer status;

    private Object data;

    private String message;

    public ResponseDto(Integer status) {
        this.status = status;
    }

    public ResponseDto(Object data) {
        this.data = data;
    }

    public ResponseDto(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

}
