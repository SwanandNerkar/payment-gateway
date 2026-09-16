package com.swanand.razorpay.payment.service.impl;

import com.swanand.razorpay.common.enums.OrderStatus;
import com.swanand.razorpay.common.exception.BusinessRuleViolationException;
import com.swanand.razorpay.common.exception.DuplicateResourceException;
import com.swanand.razorpay.common.exception.ResourceNotFoundException;
import com.swanand.razorpay.payment.dto.request.CreateOrderRequest;
import com.swanand.razorpay.payment.dto.response.OrderResponse;
import com.swanand.razorpay.payment.dto.response.PaymentResponse;
import com.swanand.razorpay.payment.entity.OrderRecord;
import com.swanand.razorpay.payment.entity.Payment;
import com.swanand.razorpay.payment.mapper.OrderMapper;
import com.swanand.razorpay.payment.mapper.PaymentMapper;
import com.swanand.razorpay.payment.repository.OrderRepository;
import com.swanand.razorpay.payment.repository.PaymentRepository;
import com.swanand.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

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
                .status(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt() :
                        LocalDateTime.now().plusMinutes(defaultOrderExpiryMinutes))
                .build();

        orderRecord = orderRepository.save(orderRecord);

        // TODO : publish kafka event

        return orderMapper.toResponse(orderRecord);
    }

    /*
    why merchantId
    because any merchant can't or shouldn't see any other merchants order
    so check whether provided order is of merchant else not valid
     */
    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.PAID) {
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Can not cancel order with status : "+ order.getStatus().name());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("order", orderId));

        List<Payment> payments = paymentRepository.findByOrder_Id(order);
        return paymentMapper.toResponseList(payments);
    }
}
