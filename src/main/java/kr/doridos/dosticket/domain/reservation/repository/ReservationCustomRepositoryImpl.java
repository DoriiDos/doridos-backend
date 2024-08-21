package kr.doridos.dosticket.domain.reservation.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.doridos.dosticket.domain.reservation.dto.QReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.QReservationResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationInfoResponse;
import kr.doridos.dosticket.domain.reservation.dto.ReservationResponse;
import kr.doridos.dosticket.domain.reservation.entity.QReservation;
import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import kr.doridos.dosticket.domain.schedule.entity.QSchedule;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.ticket.entity.QTicket;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ReservationInfoResponse findByReservationInfo(Reservation reservation) {
        QTicket qTicket = QTicket.ticket;
        QSchedule qSchedule = QSchedule.schedule;
        QReservation qReservation = QReservation.reservation;

        List<Long> seatIds = reservation.getSeats().stream()
                .map(ScheduleSeat::getId)  // ScheduleSeat 객체에서 id를 추출합니다.
                .collect(Collectors.toList());

        Expression<List<Long>> seatIdsExpression = Expressions.constant(seatIds);

        return jpaQueryFactory
                .select(new QReservationInfoResponse(
                        qReservation.id,
                        qTicket.title,
                        qTicket.content,
                        qTicket.runningTime,
                        qSchedule.startTime,
                        qSchedule.endTime,
                        seatIdsExpression
                ))
                .from(qReservation)
                .leftJoin(qTicket).on(qReservation.ticketId.eq(qTicket.id))
                .leftJoin(qSchedule).on(qReservation.scheduleId.eq(qSchedule.id))
                .where(qReservation.id.eq(reservation.getId()))
                .fetchOne();
    }
}
