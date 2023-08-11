package kr.doridos.dosticket.domain.schedule.controller;

import kr.doridos.dosticket.domain.auth.support.jwt.UserDetailsImpl;
import kr.doridos.dosticket.domain.schedule.dto.ScheduleCreateRequest;
import kr.doridos.dosticket.domain.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<Void> createSchedule(@RequestBody final ScheduleCreateRequest request,
                                               @AuthenticationPrincipal final UserDetailsImpl userDetails) {
        final Long scheduleId = scheduleService.createScheduleWithSeats(request, userDetails.getUser());
        return ResponseEntity.created(URI.create("/schedules" + scheduleId)).build();
    }
}
