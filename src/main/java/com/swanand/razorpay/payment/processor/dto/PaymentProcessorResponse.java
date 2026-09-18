package com.swanand.razorpay.payment.processor.dto;

import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.common.enums.PaymentStatus;
import com.swanand.razorpay.payment.processor.PaymentProcessor;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Success,
        PaymentProcessorResponse.Failure {

    record Pending(String processorReference) implements PaymentProcessorResponse {}

    record Success(String processorReference, String bankReference) implements PaymentProcessorResponse {}

    record Failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
