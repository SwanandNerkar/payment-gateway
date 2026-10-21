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
public class UpiPaymentAdapter implements PaymentAdapter {

    private final PaymentProcessorRouter paymentProcessorRouter;
    
    @Override
    public PaymentResult initiate(PaymentRequest request) {
        log.info("Initiate Payment with UpiPaymentAdapter, paymentId : " + request.paymentId());
        try {
            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest.nonCard(
                    request.paymentId(),
                    request.amount(),
                    request.methodDetails(),
                    PaymentMethod.UPI
            );

            PaymentProcessorResponse paymentProcessorResponse =
                    paymentProcessorRouter.charge(paymentProcessorRequest);

            return switch (paymentProcessorResponse){
                case PaymentProcessorResponse.Failure failure ->
                        new PaymentResult.Failure(failure.errorCode(), failure.errorDescription());
                case PaymentProcessorResponse.Pending pending ->
                        new PaymentResult.Pending(pending.processorReference());
                case PaymentProcessorResponse.Success success ->
                        new PaymentResult.Success(success.bankReference());
            };
        } catch (Exception e) {
            log.warn("UpiPayment failed, paymentId: {}", request.paymentId());
            return new PaymentResult.Failure("UPI_FAILED", e.getMessage());
        }
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return new PaymentResult.Success("UPI_REF");
    }
}
