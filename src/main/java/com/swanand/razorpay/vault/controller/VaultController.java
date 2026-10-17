package com.swanand.razorpay.vault.controller;

import com.swanand.razorpay.vault.dto.request.TokenizeRequest;
import com.swanand.razorpay.vault.dto.response.TokenizeResponse;
import com.swanand.razorpay.vault.service.VaultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/vault")
public class VaultController {

    private final VaultService vaultService;
    UUID merchantId = UUID.fromString("9b7f32cc-92b2-4e62-94d1-603ad09c4aa0");  // TODO : replace it with merchant id


    @PostMapping("/tokenize")
    public ResponseEntity<TokenizeResponse> tokenize(@Valid @RequestBody TokenizeRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(vaultService.tokenize(request, merchantId));
    }
}
