package com.rnd.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse {

    private String message;
    private Integer status;
    private Object data;

    public static RestResponse notfound(){
        return RestResponse.builder()
                .data(null)
                .message("sorry data not found")
                .status(404)
                .build();
    }

    public static RestResponse ok(Object data){
        return RestResponse.builder()
                .data(data)
                .status(200)
                .message("ok")
                .build();
    }
}
