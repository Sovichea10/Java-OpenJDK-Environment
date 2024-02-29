package com.camcyber.service.serviceimps;

import com.camcyber.dtos.requests.OrderRequest;
import com.camcyber.dtos.requests.ProductOrderRequest;
import com.camcyber.dtos.responses.OrderResponse;
import com.camcyber.dtos.responses.ProductOrderResponse;
import com.camcyber.entities.OrderDetailEntity;
import com.camcyber.entities.OrderEntity;
import com.camcyber.entities.ProductEntity;
import com.camcyber.entities.UserEntity;
import com.camcyber.repositories.OrderDetailRepository;
import com.camcyber.repositories.OrderRepository;
import com.camcyber.repositories.ProductRepository;
import com.camcyber.repositories.UserRepository;
import com.camcyber.service.OrderService;
import com.camcyber.shares.ReceiptNumberGenerator;
import com.camcyber.shares.exception.BadRequestException;
import com.camcyber.shares.exception.InternalServerException;
import com.camcyber.shares.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public OrderResponse invoice(OrderRequest request) {

        OrderResponse orderResponse = new OrderResponse();
        List<ProductOrderResponse> productResponses = new ArrayList<>();

        UserEntity cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(()->new NotFoundException("Cashier not found."));

        List<ProductEntity> products = new ArrayList<>();

        OrderEntity order = new OrderEntity();
        order.setCreatedAt(LocalDateTime.now());
        order.setReceiptNumber(ReceiptNumberGenerator.generateReceiptNumber());
        order.setUser(cashier);
        order.setDiscount(request.getDiscount()!=0? request.getDiscount() : 0);

        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        OrderDetailEntity orderDetail = new OrderDetailEntity();
        orderDetail.setCreatedAt(LocalDateTime.now());
        List<Double> originalPrice = new ArrayList<>();


        List<Double> eachPrice = new ArrayList<>();
        for (ProductOrderRequest productOrderRequest : request.getProductIds()){
            ProductEntity product = productRepository.findByIdAndIsDeletedFalse(productOrderRequest.getId())
                    .orElseThrow(()->new NotFoundException("Product's id "+productOrderRequest.getId()+" not found."));

            // calculate stock product
            if (product.getQuantity() -productOrderRequest.getQuantity()>0){
                product.setQuantity(product.getQuantity() -productOrderRequest.getQuantity());
                try {
                    productRepository.save(product);
                }catch (Exception e){
                    e.printStackTrace();
                    throw  new InternalServerException("Internal Server Error.");
                }
            }else throw new BadRequestException("Product's id = "+productOrderRequest.getId()+"out of stock.");

//          // calculate price product
            double unitPrice = product.getUnitPrice()*productOrderRequest.getQuantity();
            originalPrice.add(unitPrice);
            double productPrice;
            if (product.getDiscount() > 0) {
                double discountAmount = (product.getUnitPrice() * product.getDiscount()) / 100;
                productPrice = (product.getUnitPrice() - discountAmount)* productOrderRequest.getQuantity();

            }else {
                productPrice = product.getUnitPrice() * productOrderRequest.getQuantity();
            }
            if (Objects.nonNull(request.getDiscount())){
                productPrice = productPrice-((productPrice*request.getDiscount())/100);
            }
            eachPrice.add(productPrice);


            // collect all product entity
            products.add(product);

            // add to order detail table
            orderDetail.setProduct(product);
            orderDetail.setDiscount(product.getDiscount());
            orderDetail.setQuantity(productOrderRequest.getQuantity());
            orderDetails.add(orderDetail);

            ProductOrderResponse productOrderResponse = new ProductOrderResponse();
            productOrderResponse.setId(product.getId());
            productOrderResponse.setName(product.getName());
            productOrderResponse.setQuantity(productOrderRequest.getQuantity());
            productOrderResponse.setUnitPrice(product.getUnitPrice());
            productOrderResponse.setDiscountPrice(unitPrice-productPrice);
            productOrderResponse.setDiscount(product.getDiscount());
            productOrderResponse.setAmount(productPrice);
            productResponses.add(productOrderResponse);

        }

        // total price discount
        double totalPrice = 0;
        for (Double number : eachPrice) {
            totalPrice += number;
        }

        // total price original
        double originalTotalPrice = 0;
        for (Double num : originalPrice) {
            originalTotalPrice += num;
        }


        order.setTotalPrice(totalPrice);
        OrderEntity orderSave = new OrderEntity();
        try {
            orderSave = orderRepository.save(order);
        }catch (Exception e){
            e.printStackTrace();
            throw  new InternalServerException("Internal Server Error.");
        }

        // add order entity to order detail
        List<OrderDetailEntity> orderDetailAddOrderList = new ArrayList<>();
        for (OrderDetailEntity orderDetail1 : orderDetails){
            orderDetail1.setOrder(orderSave);
            orderDetailAddOrderList.add(orderDetail1);
        }

        try {
            orderDetailRepository.saveAll(orderDetailAddOrderList);
        }catch (Exception e){
            e.printStackTrace();
            throw  new InternalServerException("Internal Server Error.");
        }

        orderResponse.setId(orderSave.getId());
        orderResponse.setReceiptNumber(orderSave.getReceiptNumber());
        orderResponse.setAmountDollar(totalPrice);
        orderResponse.setTotalDollar(originalTotalPrice);
        orderResponse.setCashierId(cashier.getId());
        orderResponse.setCashierName(cashier.getName());
        orderResponse.setReceiptDate(orderSave.getCreatedAt());
        orderResponse.setProducts(productResponses);
        return orderResponse;
    }
}
