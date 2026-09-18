package com.swanand.razorpay.payment.processor.strategy;

import com.swanand.razorpay.common.util.RandomizerUtil;
import com.swanand.razorpay.payment.processor.PaymentProcessor;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        final String VPA_CODE_FAIL = "fail@okaiz";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("vpa").toString() : null;

        // simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("UPI_REJECTED",
                    "Banked rejected the transaction registration");
        }

        // we create
        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        String bankRef = "BANK_REF"+RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Success(processorRef, bankRef);
    }
}
