package com.rnd.v2.service;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;
import com.rnd.v2.base.QuarkusBase;
import com.rnd.v2.entity.Roles;
import com.rnd.v2.model.RoleRequest;
import com.rnd.v2.model.RoleResponse;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class CreateRoleService implements QuarkusBase<RoleRequest, RoleResponse>{

	@Override
	public Uni<RoleResponse> execute(RoleRequest request) {
		log.info("create role->{}",request.toString());
		Roles dataRoles = new Roles();
		dataRoles.name = request.getRoleName();
		dataRoles.activated = request.getRoleActivated();
		RoleResponse roleResponse = RoleResponse.builder()
				.roleName(dataRoles.getName())
				.roleActivated(dataRoles.getActivated())
				.build();
		return Panache.withTransaction(() -> dataRoles.persist()
						.replaceWith(roleResponse))
				.ifNoItem().after(Duration.ofMillis(10000))
				.fail().onFailure().transform(IllegalStateException::new);
	}
}
