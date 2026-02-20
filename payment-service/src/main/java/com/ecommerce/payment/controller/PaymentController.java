package com.ecommerce.payment.controller;

import com.ecommerce.payment.DTO.PaymentResponse;
import com.ecommerce.payment.DTO.PaymentStatusUpdateRequest;
import com.ecommerce.payment.DTO.RequestPayment;
import com.ecommerce.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody @Valid RequestPayment request){
        PaymentResponse response = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrderId(@PathVariable("orderId") String orderId){
        PaymentResponse response = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponse> updatedStatus(@PathVariable("paymentId") Long paymentId, @RequestBody @Valid PaymentStatusUpdateRequest request){
        PaymentResponse response  =paymentService.updatePaymentStatus(paymentId, request);
        return ResponseEntity.ok(response);
    }
}