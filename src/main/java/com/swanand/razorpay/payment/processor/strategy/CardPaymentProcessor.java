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
public class CardPaymentProcessor implements PaymentProcessor {

    public static final String PAN_CARD_DECLINED = "400000000000002";
    public static final String PAN_CARD_EXPIRED = "400000000000003";

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {

        String pan = request.pan();

        if(PAN_CARD_DECLINED.equals(pan)){
            log.warn("Card declined");
            return new PaymentProcessorResponse.Failure("CARD_DECLINED"," Card declined by");
        }

        if(PAN_CARD_EXPIRED.equals(pan)){
            log.warn("Pan Card has Expire");
            return new PaymentProcessorResponse.Failure("CARD_EXPIRED"," Card has expired");
        }

        String processorRef = "CARD_PROCESSOR_" + RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
