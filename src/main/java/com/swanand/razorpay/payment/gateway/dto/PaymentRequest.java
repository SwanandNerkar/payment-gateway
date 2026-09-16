package com.swanand.razorpay.payment.gateway.dto;

import com.swanand.razorpay.common.entity.Money;
import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.payment.dto.request.PaymentInitRequestDto;

import java.util.Map;
import java.util.UUID;

public record PaymentRequest(
        UUID paymentId,
        UUID orderId,
        UUID merchantId,
        Money amount,
        PaymentMethod method,
        Map<String, Object> methodDetails
) {
}
