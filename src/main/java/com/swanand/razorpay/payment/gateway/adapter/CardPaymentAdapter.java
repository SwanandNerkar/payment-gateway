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

        PaymentProcessorResponse response = vaultService.charge(
                request.paymentId(), token, request.amount(), request.methodDetails()
        );

//        return switch (response){
//            case PaymentProcessorResponse.Success success -> new PaymentResult.Success(success.bankReference());
//        };

        PaymentResult result = null;

        if( response instanceof  PaymentProcessorResponse.Success success){
            result =  new PaymentResult.Success(success.bankReference());
        } else if(response instanceof PaymentProcessorResponse.Failure failure){
            result = new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
        } else if(response instanceof PaymentProcessorResponse.Pending pending){
            result = new PaymentResult.Pending(pending.processorReference());
        }

        return result;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }
}
