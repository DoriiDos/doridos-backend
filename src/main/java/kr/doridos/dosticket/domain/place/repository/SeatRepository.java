package kr.doridos.dosticket.domain.place.repository;

import kr.doridos.dosticket.domain.place.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByPlaceId(Long id);
}
