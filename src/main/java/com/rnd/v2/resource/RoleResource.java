package com.rnd.v2.resource;

import com.rnd.v2.base.BaseFindIdRequest;
import com.rnd.v2.base.BaseSearchRequest;
import com.rnd.v2.entity.Roles;
import com.rnd.v2.model.RoleRequest;
import com.rnd.v2.model.SearchRoleRequest;
import com.rnd.v2.service.CreateRoleService;
import com.rnd.v2.service.DeleteRoleService;
import com.rnd.v2.service.SearchRoleService;
import com.rnd.v2.service.UpdateRoleService;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/role")
public class RoleResource {

    @Inject
    private CreateRoleService createRoleService;

    @Inject
    private UpdateRoleService updateRoleService;

    @Inject
    private SearchRoleService searchRoleService;

    @Inject
    private DeleteRoleService deleteRoleService;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> create(RoleRequest request){
        return createRoleService.execute(request)
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> update(RoleRequest request){
        return updateRoleService.execute(request)
                .onItem().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> delete(@PathParam("id")Long id){
        return deleteRoleService.execute(BaseFindIdRequest.builder().id(id)
                        .build())
                .onItem().ifNotNull().transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> search(@QueryParam("page")Integer page,
                                @QueryParam("size")Integer size){
        SearchRoleRequest request = new SearchRoleRequest();
        request.setPage(page);
        request.setSize(size);
        return searchRoleService.execute(request).onItem()
                .transform(Response::ok)
                .onItem().transform(Response.ResponseBuilder::build);
    }

}
