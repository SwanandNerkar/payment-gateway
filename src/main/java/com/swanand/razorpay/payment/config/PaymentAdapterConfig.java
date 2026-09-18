package com.swanand.razorpay.payment.config;

import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.payment.gateway.PaymentAdapter;
import com.swanand.razorpay.payment.gateway.adapter.CardPaymentAdapter;
import com.swanand.razorpay.payment.gateway.adapter.NetBankingAdapter;
import com.swanand.razorpay.payment.gateway.adapter.UpiPaymentAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PaymentAdapterConfig {

    private final CardPaymentAdapter cardPaymentAdapter;
    private final UpiPaymentAdapter upiPaymentAdapter;
    private final NetBankingAdapter netBankingAdapter;
    @Bean
    public Map<PaymentMethod, PaymentAdapter> paymentAdapterMap(){
        return Map.of(
                PaymentMethod.CARD, cardPaymentAdapter,
                PaymentMethod.UPI, upiPaymentAdapter,
                PaymentMethod.NET_BANKING, netBankingAdapter
        );
    }
}
