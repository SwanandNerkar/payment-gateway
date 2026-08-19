package com.swanand.razorpay.merchant.dto.request;

import com.swanand.razorpay.common.enums.BusinessType;
import com.swanand.razorpay.common.enums.Environment;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateApiKeyRequest(
        Environment environment
) {

}
