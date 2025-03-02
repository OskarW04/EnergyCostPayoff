package com.example.lab03_pop.controller;

import com.example.lab03_pop.entity.Payment;
import com.example.lab03_pop.service.BillingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@AllArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/manager/create")
    public ResponseEntity<String> createPayment(
            @RequestParam int tId,
            @RequestParam Double cost){
        return ResponseEntity.ok(billingService.createPayment(tId, cost));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/getAll")
    public ResponseEntity<List<Payment>> getAllPayments(){
        return ResponseEntity.ok(billingService.getAllPayments());
    }

    @PreAuthorize("hasRole('TENANT')")
    @PostMapping("/tenant/pay")
    public ResponseEntity<String> makePayment(
            @RequestParam int paymentId){
        return ResponseEntity.ok(billingService.makePayment(paymentId));
    }

    @PreAuthorize("hasRole('TENANT')")
    @GetMapping("/tenant/currentPayments")
    public ResponseEntity<List<Payment>> getCurrentPayments(){
        return ResponseEntity.ok(billingService.getCurrentPayments());
    }

    @PreAuthorize("hasRole('TENANT')")
    @GetMapping("/tenant/paymentHistory")
    public ResponseEntity<List<Payment>> getPaymentHistory(){
        return ResponseEntity.ok(billingService.getPaymentHistory());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/manager/delete")
    public ResponseEntity<String> deletePayment(
            @RequestParam int paymentId){
        return ResponseEntity.ok(billingService.deletePayment(paymentId));
    }



}
