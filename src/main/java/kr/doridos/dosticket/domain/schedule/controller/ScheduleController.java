package kr.doridos.dosticket.domain.schedule.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleResponse;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleSeatResponse;
import kr.doridos.dosticket.domain.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/schedules")
    public ResponseEntity<Void> createSchedule(@RequestBody final ScheduleCreateRequest request,
                                               @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        final Long scheduleId = scheduleService.createScheduleWithSeats(request, userDetails.getUser());
        return ResponseEntity.created(URI.create("/schedules" + scheduleId)).build();
    }

    @GetMapping("tickets/{ticketId}/schedules")
    public ResponseEntity<List<ScheduleResponse>> findAllSchedules(@PathVariable final Long ticketId) {
        final List<ScheduleResponse> schedules = scheduleService.findAllSchedules(ticketId);
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/tickets/{ticketId}/schedules/{scheduleId}/seats")
    public ResponseEntity<List<ScheduleSeatResponse>> findAllScheduleSeats(@PathVariable final Long ticketId,
                                                                           @PathVariable final Long scheduleId) {
        final List<ScheduleSeatResponse> scheduleSeatResponses = scheduleService.findAllScheduleSeats(scheduleId);
        return ResponseEntity.ok(scheduleSeatResponses);
    }
}
