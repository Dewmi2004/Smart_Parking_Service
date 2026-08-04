package com.spms.paymentservice.service;

import com.spms.paymentservice.dto.PaymentRequest;
import com.spms.paymentservice.dto.PaymentResponse;
import com.spms.paymentservice.entity.Payment;
import com.spms.paymentservice.entity.PaymentStatus;
import com.spms.paymentservice.exception.ResourceNotFoundException;
import com.spms.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Simulates a full mock payment gateway transaction:
     *  1. Save a PENDING record (a real gateway call would take time; we
     *     model that lifecycle explicitly even though it resolves inline).
     *  2. Validate the mock card data.
     *  3. Flip to SUCCESS or FAILED and save again.
     */
    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setVehicleId(request.getVehicleId());
        payment.setParkingSpaceId(request.getParkingSpaceId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionRef(generateTransactionRef());
        payment.setMaskedCardNumber(maskCardNumber(request.getCardNumber()));

        payment = paymentRepository.save(payment); // PENDING

        String failureReason = validateCard(request);
        if (failureReason == null) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason(failureReason);
        }

        payment = paymentRepository.save(payment); // SUCCESS or FAILED
        return new PaymentResponse(payment);
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(PaymentResponse::new)
                .toList();
    }

    public PaymentResponse getPaymentById(Long id) {
        return new PaymentResponse(findPaymentOrThrow(id));
    }

    public List<PaymentResponse> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(PaymentResponse::new)
                .toList();
    }

    protected Payment findPaymentOrThrow(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
    }

    /**
     * Mock gateway validation — no real card network is involved. Returns
     * null if the card "passes", or a human-readable failure reason.
     */
    private String validateCard(PaymentRequest request) {
        String digitsOnly = request.getCardNumber().replaceAll("\\s+", "");

        if (!digitsOnly.matches("\\d{16}")) {
            return "Card number must be 16 digits";
        }
        if (!request.getCvv().matches("\\d{3,4}")) {
            return "CVV must be 3 or 4 digits";
        }

        YearMonth expiry = YearMonth.of(request.getExpiryYear(), request.getExpiryMonth());
        if (expiry.isBefore(YearMonth.now())) {
            return "Card has expired";
        }

        return null; // passes mock validation
    }

    private String maskCardNumber(String cardNumber) {
        String digitsOnly = cardNumber.replaceAll("\\s+", "");
        if (digitsOnly.length() < 4) {
            return "**** **** **** ****";
        }
        String last4 = digitsOnly.substring(digitsOnly.length() - 4);
        return "**** **** **** " + last4;
    }

    private String generateTransactionRef() {
        return "TXN-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}
