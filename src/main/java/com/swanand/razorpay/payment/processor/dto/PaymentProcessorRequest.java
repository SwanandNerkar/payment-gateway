package com.swanand.razorpay.payment.processor.dto;

import com.swanand.razorpay.common.entity.Money;
import com.swanand.razorpay.common.enums.PaymentMethod;

import java.util.Map;
import java.util.UUID;

public record PaymentProcessorRequest(
        UUID processingId,
        UUID paymentId,
        PaymentMethod method,
        Money amount,

        /*
            pan & expiry are separate as we don't want to log them with method details : PCIDSS compliance
            pan entered through vault service
         */
        String pan,
        String expiry,
        Map<String, Object> methodDetails
) {

    public static PaymentProcessorRequest card(UUID paymentId, Money amount, String pan, String expiry, Map<String, Object> details){
        return new PaymentProcessorRequest(paymentId, UUID.randomUUID(), PaymentMethod.CARD, amount,
                pan, expiry, details);
    }

    public static PaymentProcessorRequest nonCard(UUID paymentId, Money amount, Map<String, Object> details, PaymentMethod method){
        return new PaymentProcessorRequest(paymentId, UUID.randomUUID(), method, amount, null, null, details);
    }
}
