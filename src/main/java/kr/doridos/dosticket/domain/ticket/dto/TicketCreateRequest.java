package kr.doridos.dosticket.domain.ticket.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.user.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TicketCreateRequest {

    @NotBlank(message = "제목은 빈값일 수 없습니다.")
    private String title;

    @NotBlank(message = "상세 내용은 빈값일 수 없습니다.")
    private String content;

    @NotBlank(message = "runningTime 빈값일 수 없습니다.")
    private String runningTime;

    @NotBlank(message = "예매시작일은 빈값일 수 없습니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime openDate;

    @NotBlank(message = "공연마감일 빈값일 수 없습니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    @NotBlank(message = "공연시작일 빈값일 수 없습니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @NotBlank(message = "placeId 빈값일 수 없습니다.")
    private Long placeId;

    @NotBlank(message = "categoryId 빈값일 수 없습니다.")
    private Long categoryId;

    public Ticket toEntity(final Place place, final User user, final Category category) {
        return new Ticket(
                title,
                content,
                runningTime,
                openDate,
                endDate,
                startDate,
                place,
                user,
                category
        );
    }
}
