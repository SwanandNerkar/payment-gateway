package com.swanand.razorpay.payment.gateway.dto;

public sealed interface PaymentResult permits PaymentResult.Pending, PaymentResult.Failure, PaymentResult.Success {

    // this reference number is like token as payment processor works asynchronously
    record Pending(String registrationRef) implements PaymentResult{};

    record Failure(String errorCode, String errorDescription) implements PaymentResult{};

    record Success(String bankReference) implements  PaymentResult{};
}
