package com.swanand.razorpay.payment.gateway;

import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.payment.gateway.dto.PaymentRequest;
import com.swanand.razorpay.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {

    public PaymentResult initiate(PaymentRequest request);

    public PaymentResult capture(UUID paymentId);
}
