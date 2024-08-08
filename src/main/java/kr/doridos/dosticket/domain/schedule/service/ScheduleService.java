package kr.doridos.dosticket.domain.schedule.service;

import kr.doridos.dosticket.domain.place.entity.Seat;
import kr.doridos.dosticket.domain.place.repository.SeatRepository;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleResponse;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleSeatResponse;
import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.schedule.exception.DuplicateScheduleTimeException;
import kr.doridos.dosticket.domain.schedule.exception.ReservationNotStartException;
import kr.doridos.dosticket.domain.schedule.exception.ScheduleNotFoundException;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleRepository;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.OpenDateNotCorrectException;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleSeatRepository scheduleSeatRepository;

    public ScheduleService(final TicketRepository ticketRepository, final SeatRepository seatRepository,
                           final ScheduleRepository scheduleRepository, final ScheduleSeatRepository scheduleSeatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
        this.scheduleSeatRepository = scheduleSeatRepository;
    }

    public Long createScheduleWithSeats(final ScheduleCreateRequest request, final User user) {
        final Ticket ticket = ticketRepository.findById(request.getTicketId())
                .orElseThrow(() -> { throw new TicketNotFoundException(ErrorCode.TICKET_NOT_FOUND); });

        validateUserType(user);
        validateEndIsNotBeforeStart(request.getStartTime(), request.getEndTime());
        validateDuplicateScheduleTime(request.getStartTime(), request.getEndTime(), ticket);

        Schedule schedule = scheduleRepository.save(request.toEntity(ticket));
        List<Seat> seats = seatRepository.findByPlaceId(ticket.getPlace().getId());
        List<ScheduleSeat> scheduleSeats = new ArrayList<>();

        seats.forEach(seat -> {
            ScheduleSeat scheduleSeat = ScheduleSeat.builder()
                    .isReserved(false)
                    .schedule(schedule)
                    .build();
            scheduleSeats.add(scheduleSeat);
        });
        scheduleSeatRepository.saveAll(scheduleSeats);

        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> findAllSchedules(final Long ticketId) {
        final Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> { throw new TicketNotFoundException(ErrorCode.TICKET_NOT_FOUND); });

       final List<Schedule> schedules = scheduleRepository.findAllByTicketId(ticketId);
        return ScheduleResponse.from(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleSeatResponse> findAllScheduleSeats(final Long scheduleId) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> { throw new ScheduleNotFoundException(ErrorCode.SCHEDULE_NOT_FOUND); });

        validateNowTimeIsNotBeforeStart(LocalDateTime.now(), schedule.getTicket().getOpenDate());

        final List<ScheduleSeat> seats = scheduleSeatRepository.findAllByScheduleId(scheduleId);
        return ScheduleSeatResponse.from(seats);
    }

    private void validateDuplicateScheduleTime(LocalDateTime startTime, LocalDateTime endTime, Ticket ticket) {
        if(scheduleRepository.getSchedulesNumByStartTime(startTime, endTime, ticket) > 0) {
            throw new DuplicateScheduleTimeException(ErrorCode.SCHEDULE_ALREADY_EXIST);
        }
    }

    private void validateUserType(final User user) {
        if(!user.getUserType().equals(UserType.TICKET_MANAGER)) {
            throw new UserNotTicketManagerException(ErrorCode.NOT_TICKET_MANAGER);
        }
    }

    private void validateEndIsNotBeforeStart(final LocalDateTime startTime, final LocalDateTime endTime) {
        if(endTime.isBefore(startTime)) {
            throw new OpenDateNotCorrectException(ErrorCode.DATE_NOT_CORRECT);
        }
    }

    private void validateNowTimeIsNotBeforeStart(final LocalDateTime nowDate, final LocalDateTime openDate) {
        if(nowDate.isBefore(openDate)) {
            throw new ReservationNotStartException(ErrorCode.RESERVATION_NOT_START);
        }
    }
}
