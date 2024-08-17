package kr.doridos.dosticket.domain.reservation.concurrency;

import kr.doridos.dosticket.domain.reservation.dto.ReservationRequest;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.reservation.repository.ReservationRepository;
import kr.doridos.dosticket.domain.reservation.service.ReservationService;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleFixture;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleSeatFixture;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleRepository;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.domain.ticket.fixture.TicketFixture;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import kr.doridos.dosticket.domain.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.concurrent.*;

@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    ReservationService reservationService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ScheduleSeatRepository scheduleSeatRepository;

    @BeforeEach
    void setup() {
        userRepository.save(UserFixture.관리자_생성());
        ticketRepository.save(TicketFixture.티켓_생성());
        scheduleRepository.save(ScheduleFixture.스케줄_생성());
        scheduleSeatRepository.save(ScheduleSeatFixture.좌석생성());
    }

    @Test
    void 티켓예매시_좌석_동시성_테스트를_진행한다() throws InterruptedException {
        List<Long> seatIds = List.of(1L);
        ReservationRequest request = new ReservationRequest(1L, 1L, seatIds);

        int threadCount = 50;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    reservationService.registerReservation(1L, request);
                }  catch (Exception e) {
                    e.printStackTrace();
                } finally{
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();
        latch.await();

        List<Reservation> reservations = reservationRepository.findAll();
        Assertions.assertThat(reservations.size()).isEqualTo(1);
    }
}
