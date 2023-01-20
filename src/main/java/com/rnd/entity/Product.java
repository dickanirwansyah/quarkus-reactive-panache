package com.rnd.entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@Cacheable
public class Product extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;
    @Column(name = "title")
    public String title;
    @Column(name = "description")
    public String description;
    @Column(name = "created_at")
    public ZonedDateTime createdAt;
    @Column(name = "updated_at")
    public ZonedDateTime updatedAt;


    public String toString(){
        return this.getClass().getSimpleName() + "<"+this.id +">";
    }


    public static Uni<Product> findByProductId(Long id){
        log.info("get product by id={}",id);
        return findById(id);
    }

    public static Uni<Product> updateProduct(Long id, Product product){
        log.info("update product by id={} and payload request={}",id,product);
        return Panache
                .withTransaction(() -> findByProductId(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.description = product.description;
                            entity.title = product.title;
                            entity.updatedAt = ZonedDateTime.now();
                            return entity;
                        }).onFailure().recoverWithNull());
    }

    public static Uni<Product> addProduct(Product product){
        log.info("add product payload={}",product);
        product.createdAt = ZonedDateTime.now();
        product.updatedAt = ZonedDateTime.now();
        return Panache.withTransaction(product::persist)
                .replaceWith(product)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure().transform(t -> new IllegalStateException(t));
    }

    public static Uni<List<PanacheEntityBase>> getAllProducts(){
        log.info("list product");
        return Product
                .listAll(Sort.by("createdAt"))
                .ifNoItem().after(Duration.ofMillis(10000))
                .fail().onFailure()
                .recoverWithUni(Uni.createFrom()
                	.<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));
    }

    public static Uni<Boolean> deleteProduct(Long id){
        log.info("delete product by id={}",id);
        return Panache.withTransaction(() -> deleteById(id));
    }
}
