package com.swanand.razorpay.payment.controller;

import com.swanand.razorpay.payment.dto.request.PaymentInitRequestDto;
import com.swanand.razorpay.payment.dto.response.PaymentResponse;
import com.swanand.razorpay.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    UUID merchantId = UUID.fromString("19f8a6b8-c0de-4288-83d3-cd4680ed3e94");  //TODO : take it from token

    @PostMapping()
    public ResponseEntity<PaymentResponse> initiate(@RequestBody @Valid PaymentInitRequestDto request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.initiate(merchantId, request));
    }

    @PostMapping("/{paymentId}/capture")
    public ResponseEntity<PaymentResponse> capture(@PathVariable UUID paymentId){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.capture(merchantId,paymentId));
    }
}
