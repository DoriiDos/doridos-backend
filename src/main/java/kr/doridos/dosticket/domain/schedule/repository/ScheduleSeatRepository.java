package kr.doridos.dosticket.domain.schedule.repository;

import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;

public interface ScheduleSeatRepository extends JpaRepository<ScheduleSeat, Long> {

    @EntityGraph(attributePaths = "schedule")
    List<ScheduleSeat> findAllByScheduleId(Long scheduleId);

}
