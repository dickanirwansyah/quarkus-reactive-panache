package com.rnd.v2.service;

import com.rnd.v2.base.BaseFindIdRequest;
import com.rnd.v2.base.BaseValidResponse;
import com.rnd.v2.base.QuarkusBase;
import com.rnd.v2.entity.Roles;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;

@Slf4j
@ApplicationScoped
public class DeleteRoleService implements QuarkusBase<BaseFindIdRequest, BaseValidResponse> {

    @Override
    public Uni<BaseValidResponse> execute(BaseFindIdRequest request) {
        log.info("request delete by id={}",request.getId());
        Uni<Roles> findIdRole = Roles.findRolesById(request.getId())
                .onFailure().recoverWithNull();
        Uni<Boolean> deleteRoleById = Roles.deleteById(request.getId())
                .onFailure().recoverWithNull();
        Uni<Tuple2<Boolean, Roles>> responseFromTuple = Uni.combine().all()
                .unis(deleteRoleById, findIdRole)
                .asTuple();

        return Panache.withTransaction(() -> responseFromTuple
                .onItem().ifNotNull().transform(entity -> {

                    if (entity.getItem1() == null || entity.getItem2() == null){
                        log.info("error because id={} not found",request.getId());
                        throw new WebApplicationException("sorry failed delete roles because id not found ",404);
                    }

                    return BaseValidResponse.builder()
                            .id(entity.getItem2().getId())
                            .valid(entity.getItem1())
                            .build();
                }));
    }
}
