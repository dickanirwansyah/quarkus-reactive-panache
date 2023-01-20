package com.rnd.v2.model;

import com.rnd.v2.base.BaseRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest extends BaseRequest{

	private Long roleId;
	private String roleName;
	private Integer roleActivated;
	
}
