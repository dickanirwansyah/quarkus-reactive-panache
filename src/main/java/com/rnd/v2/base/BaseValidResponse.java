package com.rnd.v2.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseValidResponse extends BaseResponse{
    private Long id;
    private Boolean valid;
}
