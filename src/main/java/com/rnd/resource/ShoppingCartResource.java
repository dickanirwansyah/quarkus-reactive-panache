package com.rnd.resource;

import com.rnd.entity.ShoppingCart;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/v1/carts")
public class ShoppingCartResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getCarts(){
        return ShoppingCart.getAllShoppingCarst()
                .onItem().transform(shoppingCarts -> Response.ok(shoppingCarts))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    @Produces
    public Uni<Response> createShoppingCart(ShoppingCart cart){
        if (cart == null || cart.name == null){
            throw new WebApplicationException("shopping cart name was  not set on request", 422);
        }
        return ShoppingCart.createShoppingCart(cart)
                .onItem().transform(shoppingCart -> Response.ok(shoppingCart))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @PUT
    @Path("/{cartId}/{productId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> addProductToCart(@PathParam("cartId")Long cartId, @PathParam("productId")Long productId){
        return ShoppingCart.addProductToShopping(cartId, productId)
                .onItem().ifNotNull().transform(entity -> Response.ok(entity).build())
                .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND)::build);
    }
}
