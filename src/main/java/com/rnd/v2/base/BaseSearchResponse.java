package com.rnd.v2.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseSearchResponse<T> extends BaseResponse{
    private T data;
    private Integer currentPage;
    private Long totalPages;
}
