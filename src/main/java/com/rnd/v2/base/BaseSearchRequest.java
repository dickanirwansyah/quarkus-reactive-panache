package com.rnd.v2.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchRequest extends BaseRequest{
    /** payload to search data **/
    private Integer page;
    private Integer size;
}
