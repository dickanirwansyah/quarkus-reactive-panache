package com.rnd.v2.entity;

import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.rnd.v2.base.BaseSearchResponse;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.mutiny.tuples.Tuple3;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Roles extends PanacheEntityBase{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;
	
	@Column(name = "name", nullable = false)
	public String name;
	
	@Column(name = "activated", nullable = false)
	public Integer activated;

	public static Uni<Roles> findRolesById(Long id){
		return findById(id);
	}

	public static Uni<BaseSearchResponse> searchRoles(Integer page, Integer size){
		PanacheQuery<Roles> rolesPanacheQuery = Roles.findAll();
		Uni<List<Roles>> rolesListUni = rolesPanacheQuery.page(Page.of(page, size)).list();
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
