package kr.doridos.dosticket.domain.ticket.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.doridos.dosticket.domain.category.entity.QCategory;
import kr.doridos.dosticket.domain.ticket.dto.QTicketPageResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.entity.QTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TicketCustomRepositoryImpl implements TicketCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public TicketCustomRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<TicketPageResponse> findTicketsByCategoryId(Long categoryId, Pageable pageable) {
        QCategory category = QCategory.category;
        QTicket qTicket = QTicket.ticket;

        List<Long> categoryIds = fetchCategoryIdsByParentId(categoryId, category);
        List<TicketPageResponse> ticketPageResponse = findTicketPageResponse(qTicket, pageable, qTicket.category.id.in(categoryIds));

        JPAQuery<Long> countQuery = createCountQuery(qTicket, qTicket.category.id.in(categoryIds));

        return PageableExecutionUtils.getPage(ticketPageResponse, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<TicketPageResponse> findTicketsByStartDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        QTicket qTicket = QTicket.ticket;
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<TicketPageResponse> ticketPageResponse = findTicketPageResponse(qTicket, pageable, qTicket.startDate.between(startDateTime, endDateTime));
        JPAQuery<Long> countQuery = createCountQuery(qTicket, qTicket.startDate.between(startDateTime, endDateTime));

        return PageableExecutionUtils.getPage(ticketPageResponse, pageable, countQuery::fetchOne);
    }

    private List<TicketPageResponse> findTicketPageResponse(QTicket qTicket, Pageable pageable, Predicate condition) {
        return jpaQueryFactory
                .select(new QTicketPageResponse(
                        qTicket.id,
                        qTicket.title,
                        qTicket.content,
                        qTicket.runningTime,
                        qTicket.openDate,
                        qTicket.endDate,
                        qTicket.startDate
                ))
                .from(qTicket)
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qTicket.startDate.asc())
                .fetch();
    }

    private JPAQuery<Long> createCountQuery(QTicket qTicket, Predicate condition) {
        return jpaQueryFactory
                .select(qTicket.count())
                .from(qTicket)
                .where(condition);
    }

    private List<Long> fetchCategoryIdsByParentId(Long categoryId, QCategory category) {
        return jpaQueryFactory
                .select(category.id)
                .from(category)
                .where(category.parent.id.eq(categoryId).or(category.id.eq(categoryId)))
                .fetch();
    }
}

