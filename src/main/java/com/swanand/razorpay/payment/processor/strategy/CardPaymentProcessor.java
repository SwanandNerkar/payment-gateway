package com.swanand.razorpay.payment.processor.strategy;

import com.swanand.razorpay.payment.processor.PaymentProcessor;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
