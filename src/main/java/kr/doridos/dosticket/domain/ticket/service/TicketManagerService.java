package kr.doridos.dosticket.domain.ticket.service;

import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.category.exception.CategoryNotFoundException;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.place.entity.Place;
import kr.doridos.dosticket.domain.place.repository.PlaceRepository;
import kr.doridos.dosticket.domain.ticket.dto.TicketCreateRequest;
import kr.doridos.dosticket.domain.ticket.entity.Ticket;
import kr.doridos.dosticket.domain.ticket.exception.OpenDateNotCorrectException;
import kr.doridos.dosticket.domain.ticket.exception.PlaceNotFoundException;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.ticket.repository.TicketRepository;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class TicketManagerService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final PlaceRepository placeRepository;

    public TicketManagerService(final TicketRepository ticketRepository, final CategoryRepository categoryRepository, final PlaceRepository placeRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.placeRepository = placeRepository;
    }

    public Long createTicket(TicketCreateRequest request, User user) {
        validateUserType(user);
        validateEndIsNotBeforeOpen(request.getOpenDate(), request.getEndDate());

        final Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> { throw new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND); });

        final Place place = placeRepository.findById(request.getPlaceId())
                .orElseThrow(() -> { throw new PlaceNotFoundException(ErrorCode.PLACE_NOT_FOUND); });

        final Ticket ticket = request.toEntity(place, user, category);
        ticketRepository.save(ticket);

        return ticket.getId();
    }

    private void validateUserType(final User user) {
        if(!user.getUserType().equals(UserType.TICKET_MANAGER)) {
            throw new UserNotTicketManagerException(ErrorCode.NOT_TICKET_MANAGER);
        }
    }

    private void validateEndIsNotBeforeOpen(final LocalDateTime openDate, final LocalDateTime endDate) {
        if(endDate.isBefore(openDate)) {
            throw new OpenDateNotCorrectException(ErrorCode.DATE_NOT_CORRECT);
        }
    }
}
