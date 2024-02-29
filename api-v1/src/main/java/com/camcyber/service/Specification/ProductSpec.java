package com.camcyber.service.Specification;

import com.camcyber.dtos.requests.HelperRequest;
import com.camcyber.entities.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ProductSpec {
    public static Specification<ProductEntity> getWithSearch(HelperRequest request){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (Objects.nonNull(request.getKeyword())){
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                "%"+request.getKeyword().toLowerCase().trim()+"%")
                ));
            }
            query.orderBy(criteriaBuilder.desc(root.get("id")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
