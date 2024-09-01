package kr.doridos.dosticket.domain.reservation.entity;

import jakarta.persistence.*;
import kr.doridos.dosticket.domain.schedule.entity.ScheduleSeat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table
@Entity
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long scheduleId;

    @Column(nullable = false)
    private Long ticketId;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private List<ScheduleSeat> seats = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

}
