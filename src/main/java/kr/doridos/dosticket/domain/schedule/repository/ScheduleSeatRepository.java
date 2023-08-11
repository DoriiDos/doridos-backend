package kr.doridos.dosticket.domain.schedule.repository;

import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {
}
