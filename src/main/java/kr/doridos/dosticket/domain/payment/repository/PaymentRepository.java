package kr.doridos.dosticket.domain.payment.repository;

import kr.doridos.dosticket.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
