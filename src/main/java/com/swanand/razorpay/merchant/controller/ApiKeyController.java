package com.swanand.razorpay.merchant.controller;

import com.swanand.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.swanand.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.swanand.razorpay.merchant.service.ApiKeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/merchant/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping()
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable UUID merchantId,
                                                                @Valid @RequestBody CreateApiKeyRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiKeyService.create(merchantId, request));
    }
}
