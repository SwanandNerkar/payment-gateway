package com.swanand.razorpay.vault.dto.request;

import com.swanand.razorpay.vault.validation.ExpiryYear;
import com.swanand.razorpay.vault.validation.NotExpire;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.LuhnCheck;

import java.util.UUID;

@NotExpire
public record TokenizeRequest(

        @NotBlank(message = "PAN is required")
        @LuhnCheck(message = "Invalid card number")
        @Pattern(regexp = "^[0-9]{13,19}$")
        String pan,

        @NotBlank(message = "cvv is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "cvv length is invalid")
        String cvv,

        @NotNull(message = "Expiry month required")
        @Min(value = 1, message = "expiry month must be between 1 to 12")
        @Max(value = 12, message = "expiry month must be between 1 to 12")
        Integer expiryMonth,

        @NotNull(message = "Expiry year is required")
//        @Min(value = 2026, message = "expiry month must be between 1 to 12")
        @ExpiryYear
        Integer expiryYear,

        UUID customerId,

        @Size(min = 2, message = "At least 2 characters long")
        String cardHolderName
) {
}
