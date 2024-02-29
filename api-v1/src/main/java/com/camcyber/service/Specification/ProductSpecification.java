package com.camcyber.service.Specification;

import com.camcyber.dtos.requests.HelperRequest;
import com.camcyber.entities.ProductEntity;
import com.camcyber.repositories.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductSpecification {

    private final ProductRepository productRepository;
    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    public ProductSpecification(ProductRepository productRepository, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<ProductEntity> findAllWithFilters(HelperRequest request){
        CriteriaQuery<ProductEntity> criteriaQuery = criteriaBuilder.createQuery(ProductEntity.class);
        Root<ProductEntity> productEntityRoot = criteriaQuery.from(ProductEntity.class);
        Predicate predicate = getPredicate(request.getKeyword(),productEntityRoot);
        criteriaQuery.where(predicate);
        setOrder(request,criteriaQuery,productEntityRoot);
        TypedQuery<ProductEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(request.getPage()* request.getSize());
        typedQuery.setMaxResults(request.getSize());
        Pageable pageable = getPageable(request) ;
        long productsCount=getProductCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(),pageable,productsCount);
    }

    private Predicate getPredicate(String keyword,Root<ProductEntity> productEntityRoot){
        List<Predicate> predicates = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            String attributeName = "name";  // Replace with your actual attribute name
            predicates.add(criteriaBuilder.and(
                    criteriaBuilder.isNotNull(productEntityRoot.get(attributeName)),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(productEntityRoot.get(attributeName)),
                            "%" + keyword.toLowerCase() + "%"
                    )
            ));
        }

//        predicates.add(criteriaBuilder.equal(productEntityRoot.get("isDeleted"), Boolean.FALSE));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(HelperRequest request,CriteriaQuery<ProductEntity> criteriaQuery,
                          Root<ProductEntity> productEntityRoot){
        if (request.getSortDirection().equals(Sort.Direction.ASC)){
            criteriaQuery.orderBy(criteriaBuilder.asc(productEntityRoot.get(request.getSortBy())));
        }else {
            criteriaQuery.orderBy(criteriaBuilder.desc(productEntityRoot.get(request.getSortBy())));
        }
    }

    private Pageable getPageable(HelperRequest request){
        Sort sort = Sort.by(request.getSortDirection(),request.getSortBy());
        return PageRequest.of(request.getPage()-1,request.getSize(),sort);
    }

    private long getProductCount(Predicate predicate){
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ProductEntity> productEntityRoot = countQuery.from(ProductEntity.class);
        countQuery.select(criteriaBuilder.count(productEntityRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}