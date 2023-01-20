package com.rnd.v2.model;

import com.rnd.v2.base.BaseResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse extends BaseResponse{

	private Long roleId;
	private String roleName;
	private Integer roleActivated;
}
