package kr.doridos.dosticket.domain.payment.service;

import kr.doridos.dosticket.domain.payment.controller.PaymentClient;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmRequest;
import kr.doridos.dosticket.domain.payment.dto.PaymentConfirmResponse;
import kr.doridos.dosticket.domain.payment.entity.Payment;
import kr.doridos.dosticket.domain.payment.entity.PaymentStatus;
import kr.doridos.dosticket.domain.payment.exception.PaymentException;
import kr.doridos.dosticket.domain.payment.repository.PaymentRepository;
import kr.doridos.dosticket.domain.payment.service.event.PaymentConfirmedEvent;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PaymentServiceTest {

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void 결제_확인_성공() {
        Long reservationId = 1L;
        Long userId = 1L;
        PaymentConfirmRequest paymentConfirmRequest = new PaymentConfirmRequest("payment1", 10000, "paymentKey");
        PaymentConfirmResponse paymentConfirmResponse = new PaymentConfirmResponse(
                "paymentKey", "payment1", "orderName", 1000, ZonedDateTime.now(), ZonedDateTime.now(), PaymentStatus.DONE
        );

        given(paymentClient.confirmPayment(paymentConfirmRequest)).willReturn(paymentConfirmResponse);

        PaymentConfirmResponse result = paymentService.confirmPayment(paymentConfirmRequest, reservationId, userId);

        assertThat(result).isEqualTo(paymentConfirmResponse);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(eventPublisher, times(1)).publishEvent(any(PaymentConfirmedEvent.class));
    }

    @Test
    void 결제_확인_실패_예외처리() {
        Long reservationId = 1L;
        Long userId = 1L;
        PaymentConfirmRequest paymentConfirmRequest = new PaymentConfirmRequest("payment1", 10000, "paymentKey");

        given(paymentClient.confirmPayment(paymentConfirmRequest)).willThrow(new PaymentException("결제 실패", "PaymentError", HttpStatus.BAD_REQUEST));

        assertThatThrownBy(() -> paymentService.confirmPayment(paymentConfirmRequest, reservationId, userId))
                .isInstanceOf(PaymentException.class)
                .hasMessage("결제 실패");

        verify(paymentRepository, never()).save(any(Payment.class));
        verify(eventPublisher, never()).publishEvent(any(PaymentConfirmedEvent.class));
    }
}
