package com.swanand.razorpay.merchant.dto.request;

import com.swanand.razorpay.common.enums.BusinessType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(
        @NotNull(message = "name should be provided")
        @Size(max = 50, message = "name should not more than 50 character long")
        String name,

        @Email
        @NotNull(message = "Email is required")
        String email,

        @NotNull
        @Size(min = 8, message = "password should at least 8 characters long")
        String password,

        @Size(max = 50, message = "Business Name should be less than 50 characters")
        String businessName,

        BusinessType businessType
) {

}
