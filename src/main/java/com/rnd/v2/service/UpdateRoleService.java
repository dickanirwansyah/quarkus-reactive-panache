package com.rnd.v2.service;

import com.rnd.v2.base.QuarkusBase;
import com.rnd.v2.entity.Roles;
import com.rnd.v2.model.RoleRequest;
import com.rnd.v2.model.RoleResponse;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

@Slf4j
@ApplicationScoped
public class UpdateRoleService  implements QuarkusBase<RoleRequest, RoleResponse> {

    @Override
    public Uni<RoleResponse> execute(RoleRequest request) {
        return Panache.withTransaction(() -> Roles.findRolesById(request.getRoleId())
                .onItem().ifNotNull()
                .transform(roles -> {
                    roles.name = request.getRoleName();
                    roles.activated = request.getRoleActivated();
                    RoleResponse response = new RoleResponse();
                    response.setRoleId(roles.id);
                    response.setRoleName(roles.name);
                    response.setRoleActivated(roles.activated);
                    return response;
                }).onFailure().recoverWithNull());
    }
}
