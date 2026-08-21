package com.swanand.razorpay.merchant.service;

import com.swanand.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.swanand.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.swanand.razorpay.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotateKey(UUID merchantId, UUID keyId);
}
