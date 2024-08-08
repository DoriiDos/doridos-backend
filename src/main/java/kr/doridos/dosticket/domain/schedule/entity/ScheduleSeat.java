package kr.doridos.dosticket.domain.schedule.entity;

import kr.doridos.dosticket.domain.place.entity.Seat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class ScheduleSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean isReserved;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public ScheduleSeat(final boolean isReserved, final Seat seat, final Schedule schedule) {
        this.isReserved = isReserved;
        this.schedule = schedule;
    }
}
