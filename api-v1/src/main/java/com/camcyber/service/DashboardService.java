package com.camcyber.service;

import com.camcyber.dtos.responses.ProductResponse;

import java.util.List;

public interface DashboardService {

    Double incomeToday();
    List<ProductResponse> productNearOutStock();

}
