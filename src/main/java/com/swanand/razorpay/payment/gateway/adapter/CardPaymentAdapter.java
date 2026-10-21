package com.swanand.razorpay.payment.gateway.adapter;

import com.swanand.razorpay.payment.gateway.PaymentAdapter;
import com.swanand.razorpay.payment.gateway.dto.PaymentRequest;
import com.swanand.razorpay.payment.gateway.dto.PaymentResult;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.swanand.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CardPaymentAdapter implements PaymentAdapter {

    private final VaultService vaultService;

    @Override
    public PaymentResult initiate(PaymentRequest request) {
        String token = (String) request.methodDetails().get("token");

        PaymentProcessorResponse paymentProcessorResponse = vaultService.charge(
                request.paymentId(), token, request.amount(), request.methodDetails()
        );

        return switch (paymentProcessorResponse){
            case PaymentProcessorResponse.Failure failure ->
                    new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            case PaymentProcessorResponse.Pending pending ->
                    new PaymentResult.Pending(pending.processorReference());
            case PaymentProcessorResponse.Success success ->
                    new PaymentResult.Success(success.bankReference());
        };
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
