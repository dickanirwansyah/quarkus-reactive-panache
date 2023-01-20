package com.rnd.v2.base;

import io.smallrye.mutiny.Uni;

public interface QuarkusBase<R extends BaseRequest, T extends BaseResponse>{
	 Uni<T> execute(R request);
}
