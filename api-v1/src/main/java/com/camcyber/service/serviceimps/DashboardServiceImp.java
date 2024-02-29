package com.camcyber.service.serviceimps;

import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.entities.OrderEntity;
import com.camcyber.entities.ProductEntity;
import com.camcyber.repositories.OrderRepository;
import com.camcyber.repositories.ProductRepository;
import com.camcyber.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    @Override
    public Double incomeToday() {

        LocalDateTime startOfDay = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endOfDay = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        List<OrderEntity> orderEntities = orderRepository.findByCreatedAtBetween(startOfDay,endOfDay);
        List<Double> income = new ArrayList<>();
        for (OrderEntity order: orderEntities){
            income.add(order.getTotalPrice());
        }
        double total = 0;
        for (Double num : income) {
            total += num;
        }
        return total;
    }

    @Override
    public List<ProductResponse> productNearOutStock() {

        List<ProductResponse> productResponses = new ArrayList<>();
        List<ProductEntity> productEntities  = productRepository.listAllNearOutStock();
        for (ProductEntity product: productEntities){
            ProductResponse response = new ProductResponse();
            response.setId(product.getId());
            response.setCode(product.getCode());
            response.setName(product.getName());
            response.setDescription(product.getDescription());
            response.setQuantity(product.getQuantity());
            response.setUnitPrice(product.getUnitPrice());
            response.setDiscount(product.getDiscount());
            response.setCreatedDate(product.getCreatedAt());
            response.setUpdatedDate(product.getUpdatedAt());
            // TODO: 11/26/2023 read file
            response.setImage(null);
            productResponses.add(response);
        }

        return productResponses;
    }
}
