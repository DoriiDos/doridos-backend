package kr.doridos.dosticket.domain.ticket.entity;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.ticket.dto.TicketUpdateRequest;
import kr.doridos.dosticket.domain.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String runningTime;

    @Column(nullable = false)
    private LocalDateTime openDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = true)
    private LocalDateTime deleteAt;

    public Ticket(final String title,
                  final String content,
                  final String runningTime,
                  final LocalDateTime openDate,
                  final LocalDateTime endDate,
                  final LocalDateTime startDate,
                  final Place place,
                  final User user,
                  final Category category) {
        this.title = title;
        this.content = content;
        this.runningTime = runningTime;
        this.openDate = openDate;
        this.endDate = endDate;
        this.startDate = startDate;
        this.place = place;
        this.user = user;
        this.category = category;
    }

    @Builder
    public Ticket(final String title, final String content, final String runningTime, final LocalDateTime openDate,
                  final LocalDateTime endDate, final LocalDateTime startDate) {
        this.title = title;
        this.content = content;
        this.runningTime = runningTime;
        this.openDate = openDate;
        this.endDate = endDate;
        this.startDate = startDate;
    }

    public void update(final TicketUpdateRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.runningTime = request.getRunningTime();
        this.openDate = request.getOpenDate();
        this.endDate = request.getEndDate();
        this.startDate = request.getStartDate();
    }
}
