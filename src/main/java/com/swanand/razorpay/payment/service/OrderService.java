package com.swanand.razorpay.payment.service;

import com.swanand.razorpay.payment.dto.request.CreateOrderRequest;
import com.swanand.razorpay.payment.dto.response.OrderResponse;

import java.net.URI;
import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);
}
