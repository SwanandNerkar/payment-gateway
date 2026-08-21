package com.swanand.razorpay.payment.dto.request;
import com.swanand.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;
public record CreateOrderRequest(
        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt,     // order_id known to merchant
        Map<String, Object> notes,  // json type notes for merchant
        LocalDateTime expiresAt
) {
}
