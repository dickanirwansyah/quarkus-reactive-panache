package com.rnd.resource;

import com.rnd.entity.RestResponse;
import com.rnd.entity.Product;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/product")
public class ProductResource {

    @GET
    @Path("/list-product")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> listProduct(){
        return Product.getAllProducts()
                .onItem().transform(products -> Response.ok(RestResponse.ok(products)))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    @Path("/save-product")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> saveProduct(Product product){
        return Product.addProduct(product)
                .onItem().transform(productResponse -> Response
                        .ok(RestResponse.ok(productResponse)))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @GET
    @Path("/get-byid/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getById(@PathParam("id")Long id){
        return Product.findByProductId(id)
                .onItem().ifNotNull().transform(product -> Response
                        .ok(RestResponse.ok(product)).build())
                .onItem().ifNull().continueWith(Response.ok(RestResponse.notfound())
                        .status(Response.Status.NOT_FOUND)::build);
    }
}

