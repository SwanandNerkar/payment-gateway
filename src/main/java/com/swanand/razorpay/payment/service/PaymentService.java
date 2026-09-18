package com.swanand.razorpay.payment.service;

import com.swanand.razorpay.payment.dto.request.PaymentInitRequestDto;
import com.swanand.razorpay.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequestDto request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);
}
