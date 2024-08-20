package kr.doridos.dosticket.domain.reservation.repository;

import kr.doridos.dosticket.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationCustomRepository {

    @Query("select count(r) from Reservation r join r.seats s where s.id = :seatId")
    int countReservationsByScheduleSeatId(@Param("seatId") Long seatId);

}
