package com.camcyber.rest_controller;

import com.camcyber.dtos.responses.BaseResponse;
import com.camcyber.dtos.responses.ProductResponse;
import com.camcyber.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardRestController {
    private final DashboardService dashboardService;

    @GetMapping("/income-today")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<Map<String,Double>>> incomeToday(){

        Map<String,Double> responseMap = new HashMap<>();
        responseMap.put("Total",dashboardService.incomeToday());
        BaseResponse<Map<String,Double>> response = new BaseResponse<>();
        response.setData(responseMap);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product-outstock")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('CASHIER')")
    public ResponseEntity<BaseResponse<List<ProductResponse>>> productOutStock(){

        List<ProductResponse> productResponses = dashboardService.productNearOutStock();
        BaseResponse<List<ProductResponse>> response = new BaseResponse<>();
        response.setData(productResponses);
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }




}
