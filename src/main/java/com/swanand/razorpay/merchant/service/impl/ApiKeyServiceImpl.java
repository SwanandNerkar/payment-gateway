package com.swanand.razorpay.merchant.service.impl;

import com.swanand.razorpay.common.exception.ResourceNotFoundException;
import com.swanand.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.swanand.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.swanand.razorpay.merchant.entity.ApiKey;
import com.swanand.razorpay.merchant.entity.Merchant;
import com.swanand.razorpay.merchant.repository.ApiKeyRepository;
import com.swanand.razorpay.merchant.repository.MerchantRepository;
import com.swanand.razorpay.merchant.service.ApiKeyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toUpperCase()+"big_random_string";
        String rawSecret = "bigRandomSecret";   // TODO : replace with cryptographic text

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecret(rawSecret)
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }
}
