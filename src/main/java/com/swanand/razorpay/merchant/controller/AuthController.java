package com.swanand.razorpay.merchant.controller;

import com.swanand.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.swanand.razorpay.merchant.dto.response.MerchantResponse;
import com.swanand.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<MerchantResponse> signup(@RequestBody MerchantSignupRequest request){

        return new ResponseEntity<>(new MerchantResponse(), HttpStatus.OK);
    }
}
