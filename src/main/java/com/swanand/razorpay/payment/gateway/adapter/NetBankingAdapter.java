package com.swanand.razorpay.payment.gateway.adapter;

import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.payment.gateway.PaymentAdapter;
import com.swanand.razorpay.payment.gateway.dto.PaymentRequest;
import com.swanand.razorpay.payment.gateway.dto.PaymentResult;
import com.swanand.razorpay.payment.processor.PaymentProcessorRouter;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class NetBankingAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        log.info("Initiate Payment with NetBankingAdapter, paymentId : "+ request.paymentId());
        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    request.paymentId(),
                    request.amount(),
                    request.methodDetails(),
                    PaymentMethod.NET_BANKING
            );

            PaymentProcessorResponse paymentProcessorResponse =
                    paymentProcessorRouter.charge(paymentProcessorRequest);

            // FIXME : solve , install new intellij 2026
//        return switch (paymentProcessorRouter){
//            case PaymentProcessorResponse.Failure failure ->
//        };

            PaymentResult paymentResult = null;
            if (paymentProcessorResponse instanceof PaymentProcessorResponse.Failure failure) {
                paymentResult = new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
            } else if (paymentProcessorResponse instanceof PaymentProcessorResponse.Pending pending) {
                paymentResult = new PaymentResult.Pending(pending.processorReference());
            } else if (paymentProcessorResponse instanceof PaymentProcessorResponse.Success success) {
                paymentResult = new PaymentResult.Success(success.bankReference()); // processor reference didn't use
            }

            return paymentResult;
        }
        catch (Exception e){
            log.warn("NetBanking failed, paymentId: {}", request.paymentId());
            return new PaymentResult.Failure("NBK_FAILED", e.getMessage());
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("NBK_REF");
    }
}
