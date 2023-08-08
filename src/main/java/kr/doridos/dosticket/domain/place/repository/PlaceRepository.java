package kr.doridos.dosticket.domain.place.repository;

import kr.doridos.dosticket.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findById(Long id);
}
