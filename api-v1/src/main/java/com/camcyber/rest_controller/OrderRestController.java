package com.camcyber.rest_controller;

import com.camcyber.dtos.requests.OrderRequest;
import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.OrderResponse;
import com.camcyber.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderService orderService;

    @PostMapping("/invoice")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<OrderResponse>> invoice (@RequestBody OrderRequest request){

        OrderResponse orderResponse = orderService.invoice(request);

        BaseResponse<OrderResponse> response = new BaseResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData( orderResponse);

        return ResponseEntity.ok(response);
    }


}
