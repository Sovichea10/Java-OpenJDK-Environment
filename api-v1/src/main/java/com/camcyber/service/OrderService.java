package com.camcyber.service;

import com.camcyber.dtos.requests.OrderRequest;
import com.camcyber.dtos.responses.OrderResponse;

public interface OrderService {

    OrderResponse invoice(OrderRequest request);

}
