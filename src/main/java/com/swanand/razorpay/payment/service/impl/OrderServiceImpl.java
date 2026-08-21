package com.swanand.razorpay.payment.service.impl;

import com.swanand.razorpay.common.enums.OrderStatus;
import com.swanand.razorpay.common.exception.DuplicateResourceException;
import com.swanand.razorpay.payment.dto.request.CreateOrderRequest;
import com.swanand.razorpay.payment.dto.response.OrderResponse;
import com.swanand.razorpay.payment.entity.OrderRecord;
import com.swanand.razorpay.payment.repository.OrderRepository;
import com.swanand.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultOrderExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt() != null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE", "Order with receipt already exists: "+ request.receipt());
        }

        OrderRecord orderRecord = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        orderRecord = orderRepository.save(orderRecord);

        // TODO : publish kafka event

        return OrderResponse.builder()
                .id(orderRecord.getId())
                .createdAt(null)
                .amount(orderRecord.getAmount())
                .attempts(orderRecord.getAttempts())
                .expiresAt(orderRecord.getExpiresAt())
                .merchantId(orderRecord.getMerchantId())
                .notes(orderRecord.getNotes())
                .receipt(orderRecord.getReceipt())
                .status(orderRecord.getOrderStatus())
                .build();
    }
}
