package kr.doridos.dosticket.domain.ticket.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.doridos.dosticket.domain.category.entity.QCategory;
import kr.doridos.dosticket.domain.ticket.dto.QTicketPageResponse;
import kr.doridos.dosticket.domain.ticket.dto.TicketPageResponse;
import kr.doridos.dosticket.domain.ticket.entity.QTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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

        List<Long> categoryIds = jpaQueryFactory
                .select(category.id)
                .from(category)
                .where(category.parent.id.eq(categoryId).or(category.id.eq(categoryId)))
                .fetch();

        List<TicketPageResponse> ticketPageResponse = jpaQueryFactory
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
                .where(qTicket.category.id.in(categoryIds))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(qTicket.count())
                .from(qTicket)
                .where(qTicket.category.id.in(categoryIds));

        return PageableExecutionUtils.getPage(ticketPageResponse, pageable, countQuery::fetchOne);
    }
}
