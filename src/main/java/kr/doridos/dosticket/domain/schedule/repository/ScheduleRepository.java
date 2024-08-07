package kr.doridos.dosticket.domain.schedule.repository;

import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT COUNT(s) FROM Schedule s " + "WHERE s.startTime <= :endTime AND s.endTime >= :startTime " +
            "AND s.ticket = :ticket")
    int getSchedulesNumByStartTime(@Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime,
                                   @Param("ticket") Ticket ticket);

    List<Schedule> findAllByTicketId(Long ticketId);

}
