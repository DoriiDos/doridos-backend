package kr.doridos.dosticket.domain.payment.service;

import kr.doridos.dosticket.domain.payment.controller.PaymentClient;
import kr.doridos.dosticket.domain.payment.dto.PaymentCancelRequest;
import kr.doridos.dosticket.domain.payment.dto.PaymentCancelResponse;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmRequest;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmResponse;
import kr.doridos.dosticket.domain.payment.entity.Payment;
import kr.doridos.dosticket.domain.payment.exception.PaymentAlreadyProcessedException;
import kr.doridos.dosticket.domain.payment.exception.PaymentNotFoundException;
import kr.doridos.dosticket.domain.payment.service.event.PaymentCancelEvent;
import kr.doridos.dosticket.domain.payment.service.event.PaymentConfirmedEvent;
import kr.doridos.dosticket.domain.payment.repository.PaymentRepository;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class PaymentService {
    private static final String IDEMPOTENCY_KEY_PREFIX = "payment:confirm:";

    private final PaymentClient paymentClient;
    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final StringRedisTemplate redisTemplate;

    public PaymentService(final PaymentClient paymentClient, final PaymentRepository paymentRepository, final ApplicationEventPublisher eventPublisher, final StringRedisTemplate redisTemplate) {
        this.paymentClient = paymentClient;
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
        this.redisTemplate = redisTemplate;
    }

    public PaymentConfirmResponse confirmPayment(final PaymentConfirmRequest paymentConfirmRequest, final Long reservationId, final Long userId) {
        final String idempotencyKey = IDEMPOTENCY_KEY_PREFIX + paymentConfirmRequest.getOrderId();
        final Boolean isFirstRequest = redisTemplate.opsForValue().setIfAbsent(idempotencyKey, "success", 10, TimeUnit.SECONDS);

        if (!Boolean.TRUE.equals(isFirstRequest)) {
            throw new PaymentAlreadyProcessedException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }

        final PaymentConfirmResponse paymentConfirmResponse = paymentClient.confirmPayment(paymentConfirmRequest);
        final Payment payment = paymentConfirmResponse.toPayment(reservationId, userId);

        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentConfirmedEvent(reservationId));

        return paymentConfirmResponse;
    }


/*    public PaymentConfirmResponse confirmPayment(final PaymentConfirmRequest paymentConfirmRequest, final Long reservationId, final Long userId) {
        String idempotencyKey = IDEMPOTENCY_KEY_PREFIX + paymentConfirmRequest.getOrderId();
        RBucket<String> bucket = redissonClient.getBucket(idempotencyKey);

        final Boolean isFirstRequest = bucket.trySet(idempotencyKey, 10, TimeUnit.MINUTES);

        if (!Boolean.TRUE.equals(isFirstRequest)) {
            throw new PaymentAlreadyProcessedException(ErrorCode.PAYMENT_ALREADY_PROCESSED);
        }

        final PaymentConfirmResponse paymentConfirmResponse = paymentClient.confirmPayment(paymentConfirmRequest);
        final Payment payment = paymentConfirmResponse.toPayment(reservationId, userId);

        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentConfirmedEvent(reservationId));

        return paymentConfirmResponse;
    }*/

    public PaymentCancelResponse cancelPayment(final PaymentCancelRequest request, final Long paymentId) {
        final Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> { throw new PaymentNotFoundException(ErrorCode.PAYMENT_NOT_FOUND); });

        final PaymentCancelResponse response = paymentClient.cancelPayment(payment.getPaymentKey(), request);
        payment.changeStatus(response.getPaymentStatus(), response.getRequestedAt(), response.getApprovedAt());

        paymentRepository.save(payment);
        eventPublisher.publishEvent(new PaymentCancelEvent(payment.getReservationId()));

        return response;
    }
}




