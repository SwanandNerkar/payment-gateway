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
public class NetBankingPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        final String BANK_CODE_FAIL = "BANK_CODE_FAIL";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("BANK").toString() : null;

        // simulation
        if(BANK_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Banked rejected the transaction registration");
        }

        // we create
        String processorRef = "NBK_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        // banking system create, like bank reference
        /*
        they have made the payment, and they return this reference to us
         */
        String redirectRef = "http://REDIRECT_BANK.com/" + processorRef;

        // call 3rd party
        return new PaymentProcessorResponse.Success(processorRef, redirectRef);
    }
}
