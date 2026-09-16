package com.swanand.razorpay.payment.processor.config;

import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.payment.processor.PaymentProcessor;
import com.swanand.razorpay.payment.processor.strategy.CardPaymentProcessor;
import com.swanand.razorpay.payment.processor.strategy.NetBankingPaymentProcessor;
import com.swanand.razorpay.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentProcessorConfig {

    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.UPI, new UpiPaymentProcessor(),
                PaymentMethod.NET_BANKING, new NetBankingPaymentProcessor()
        );
    }
}
