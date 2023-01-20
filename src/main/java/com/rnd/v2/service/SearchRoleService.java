package com.rnd.v2.service;

import com.rnd.v2.base.BaseSearchResponse;
import com.rnd.v2.base.QuarkusBase;
import com.rnd.v2.entity.Roles;
import com.rnd.v2.model.SearchRoleRequest;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple3;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Slf4j
@ApplicationScoped
public class SearchRoleService implements QuarkusBase<SearchRoleRequest, BaseSearchResponse> {

    @Override
    public Uni<BaseSearchResponse> execute(SearchRoleRequest request) {
        PanacheQuery<Roles> rolesPanacheQuery = Roles.findAll();
        Uni<List<Roles>> rolesListUni = rolesPanacheQuery.page(Page.of(request.getPage(), request.getSize())).list();
        Uni<Long> roleCountUni =  rolesPanacheQuery.count();
        Uni<Integer> roleCurrentPageUni = rolesPanacheQuery.pageCount();

        Uni<Tuple3<List<Roles>, Long, Integer>> responseUniTuples = Uni.combine()
                .all().unis(rolesListUni, roleCountUni, roleCurrentPageUni)
                .asTuple();

        return Panache.withTransaction(()-> responseUniTuples.onItem()
                .ifNotNull().transform(entity -> {
                    return BaseSearchResponse.builder()
                            .data(entity.getItem1())
                            .totalPages(entity.getItem2())
                            .currentPage(entity.getItem3())
                            .build();
                }));
    }
}
