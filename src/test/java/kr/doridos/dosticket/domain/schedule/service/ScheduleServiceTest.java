package kr.doridos.dosticket.domain.schedule.service;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.place.repository.SeatRepository;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleResponse;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleSeatResponse;
import kr.doridos.dosticket.domain.schedule.entity.Schedule;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import kr.doridos.dosticket.domain.schedule.exception.DuplicateScheduleTimeException;
import kr.doridos.dosticket.domain.schedule.exception.ReservationNotStartException;
import kr.doridos.dosticket.domain.schedule.exception.ScheduleNotFoundException;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleFixture;
import kr.doridos.dosticket.domain.schedule.fixture.ScheduleSeatFixture;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleRepository;
import kr.doridos.dosticket.domain.schedule.repository.ScheduleSeatRepository;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.OpenDateNotCorrectException;
import kr.doridos.dosticket.domain.ticket.exception.TicketNotFoundException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.entity.User;
import kr.doridos.dosticket.domain.user.entity.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ScheduleSeatRepository scheduleSeatRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private SeatRepository seatRepository;

    ScheduleCreateRequest scheduleCreateRequest = getScheduleCreateRequest();
    User ticketManager = getTicketManager();
    Ticket ticket = getTicket();

    @Test
    @DisplayName("스케줄을 성공적으로 생성한다.")
    void createSchedule_success() {
        Schedule schedule = scheduleCreateRequest.toEntity(ticket);

        given(ticketRepository.findById(scheduleCreateRequest.getTicketId())).willReturn(Optional.of(ticket));
        given(scheduleRepository.getSchedulesNumByStartTime(
                scheduleCreateRequest.getStartTime(),
                scheduleCreateRequest.getEndTime(),
                ticket)).willReturn(0);
        given(scheduleRepository.save(any())).willReturn(schedule);
        given(seatRepository.findByPlaceId(getTicket().getPlace().getId())).willReturn(new ArrayList<>());

        Long scheduleId = scheduleService.createScheduleWithSeats(scheduleCreateRequest, ticketManager);

        then(ticketRepository).should().findById(1L);
        then(scheduleRepository).should().getSchedulesNumByStartTime(
                scheduleCreateRequest.getStartTime(),
                scheduleCreateRequest.getEndTime(),
                ticket);
        then(scheduleRepository).should().save(any(Schedule.class));
        then(seatRepository).should().findByPlaceId(any());

        assertThat(scheduleId).isEqualTo(schedule.getId());
    }

    @Test
    @DisplayName("스케줄 생성시 티켓이 없으면 예외가 발생한다.")
    void createSchedule_NotExistTicket_throwException() {
        given(ticketRepository.findById(scheduleCreateRequest.getTicketId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.createScheduleWithSeats(scheduleCreateRequest, ticketManager))
                .isInstanceOf(TicketNotFoundException.class)
                .hasMessage("티켓을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("스케줄 생성시 겹치는 시간이 있으면 예외가 발생한다.")
    void createSchedule_duplicatedTime_throwException() {
        given(ticketRepository.findById(scheduleCreateRequest.getTicketId())).willReturn(Optional.of(ticket));
        given(scheduleRepository.getSchedulesNumByStartTime(
                scheduleCreateRequest.getStartTime(),
                scheduleCreateRequest.getEndTime(),
                ticket)).willReturn(1);

        assertThatThrownBy(() -> scheduleService.createScheduleWithSeats(scheduleCreateRequest, ticketManager))
                .isInstanceOf(DuplicateScheduleTimeException.class)
                .hasMessage("해당시간에 이미 스케줄이 존재합니다.");
    }

    @Test
    @DisplayName("스케줄 생성시 시작시간과 종료시간이 유효하지 않으면 예외가 발생한다.")
    void createSchedule_notCorrectDate_throwException() {
        ScheduleCreateRequest notCollectScheduleCreateRequest = ScheduleCreateRequest.builder()
                .ticketId(1L)
                .startTime(LocalDateTime.of(2023, 7, 22, 7, 0))
                .endTime(LocalDateTime.of(2023, 7, 22, 6, 0))
                .build();

        given(ticketRepository.findById(scheduleCreateRequest.getTicketId())).willReturn(Optional.of(ticket));

        assertThatThrownBy(() -> scheduleService.createScheduleWithSeats(notCollectScheduleCreateRequest, ticketManager))
                .isInstanceOf(OpenDateNotCorrectException.class)
                .hasMessage("시작일은 종료일 이후가 될 수 없습니다.");
    }

    @Test
    void 티켓에_해당하는_스케줄을_조회한다() {
        List<Schedule> schedules = Arrays.asList(ScheduleFixture.스케줄_생성(), ScheduleFixture.스케줄_생성2());

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(scheduleRepository.findAllByTicketId(ticket.getId())).willReturn(schedules);

        List<ScheduleResponse> scheduleResponses = scheduleService.findAllSchedules(ticket.getId());

        assertThat(scheduleResponses.size()).isEqualTo(2);
        assertThat(scheduleResponses.get(0).getId()).isEqualTo(1L);
        assertThat(scheduleResponses.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void 스케줄_좌석을_조회한다() {
        Long scheduleId = ScheduleFixture.스케줄_생성().getId();
        List<ScheduleSeat> scheduleSeats = Arrays.asList(ScheduleSeatFixture.좌석생성(), ScheduleSeatFixture.좌석생성2());

        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.ofNullable(ScheduleFixture.스케줄_생성()));
        given(scheduleSeatRepository.findAllByScheduleId(scheduleId)).willReturn(scheduleSeats);

        List<ScheduleSeatResponse> scheduleSeatResponses = scheduleService.findAllScheduleSeats(scheduleId);

        assertThat(scheduleSeatResponses.size()).isEqualTo(2);
    }

    @Test
    void 티켓_오픈_시작전_좌석을_조회하면_예외가_발생한다() {
        LocalDateTime now = LocalDateTime.now();

        Ticket ticket = Ticket.builder()
                .id(1L)
                .openDate(now.plusDays(1))
                .build();
        Schedule schedule = Schedule.builder()
                .id(1L)
                .ticket(ticket)
                .build();

        given(scheduleRepository.findById(schedule.getId())).willReturn(Optional.of(schedule));

        assertThatThrownBy(() -> scheduleService.findAllScheduleSeats(schedule.getId()))
                .isInstanceOf(ReservationNotStartException.class)
                .hasMessage(ErrorCode.RESERVATION_NOT_START.getMessage());
    }

    @Test
    void 존재하지_않는_스케줄_ID로_조회하면_예외가_발생한다() {
        Long scheduleId = 99L;

        given(scheduleRepository.findById(scheduleId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> scheduleService.findAllScheduleSeats(scheduleId))
                .isInstanceOf(ScheduleNotFoundException.class)
                .hasMessage("스케줄이 존재하지 않습니다.");
    }

    private ScheduleCreateRequest getScheduleCreateRequest() {
        return new ScheduleCreateRequest(1L, LocalDateTime.of(2023, 7, 22, 12, 0), LocalDateTime.of(2023, 7, 22, 14, 0));
    }

    private User getTicketManager() {
        return new User(1L, "email@email", "123456a!", "호호", "12345", UserType.TICKET_MANAGER, LocalDateTime.now(), LocalDateTime.now(), null);
    }

    private Ticket getTicket() {
        return new Ticket(1L, "모차르트",
                "모차르트 최고의 연주",
                "120분",
                LocalDateTime.of(2023, 7, 22, 12, 0),
                LocalDateTime.of(2023, 7, 23, 12, 0),
                LocalDateTime.of(2023, 7, 23, 12, 0),
                new Place(1L, "잠실경기장", "이곳이잠실이다", LocalDateTime.now(), LocalDateTime.now()),
                getTicketManager(),
                new Category(1L, "뮤지컬"),
                LocalDateTime.now(), LocalDateTime.now(), null);
    }
}