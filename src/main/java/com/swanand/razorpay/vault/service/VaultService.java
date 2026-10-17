package com.swanand.razorpay.vault.service;

import com.swanand.razorpay.common.entity.Money;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.swanand.razorpay.vault.dto.request.TokenizeRequest;
import com.swanand.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
