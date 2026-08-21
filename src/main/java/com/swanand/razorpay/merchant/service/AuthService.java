package com.swanand.razorpay.merchant.service;

import com.swanand.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.swanand.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
