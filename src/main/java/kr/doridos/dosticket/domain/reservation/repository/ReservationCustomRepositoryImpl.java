package kr.doridos.dosticket.domain.reservation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.doridos.dosticket.domain.reservation.dto.QReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.entity.QReservation;
import kr.doridos.dosticket.domain.schedule.entity.QSchedule;
import kr.doridos.dosticket.domain.ticket.entity.QTicket;

import java.util.List;

public class ReservationCustomRepositoryImpl implements ReservationCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public ReservationCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ReservationResponse> findReservationsByUserId(Long userId) {
        QReservation qReservation = QReservation.reservation;
        QTicket qTicket = QTicket.ticket;
        QSchedule qSchedule = QSchedule.schedule;

        return jpaQueryFactory
                .select(new QReservationResponse(
                        qReservation.id,
                        qTicket.title,
                        qSchedule.startTime,
                        qSchedule.endTime
                ))
                .from(qReservation)
                .leftJoin(qTicket).on(qReservation.ticketId.eq(qTicket.id))
                .leftJoin(qSchedule).on(qReservation.scheduleId.eq(qSchedule.id))
                .where(qReservation.userId.eq(userId))
                .fetch();
    }
}
