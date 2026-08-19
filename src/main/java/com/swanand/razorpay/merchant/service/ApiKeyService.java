package com.swanand.razorpay.merchant.service;

import com.swanand.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.swanand.razorpay.merchant.dto.response.ApiKeyCreateResponse;

import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);
}
