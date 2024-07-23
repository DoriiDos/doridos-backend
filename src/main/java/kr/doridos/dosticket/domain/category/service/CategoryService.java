package kr.doridos.dosticket.domain.category.service;

import kr.doridos.dosticket.domain.category.dto.CategoryListResponse;
import kr.doridos.dosticket.domain.category.dto.CategoryRequest;
import kr.doridos.dosticket.domain.category.dto.CategoryResponse;
import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.category.exception.CategoryAlreadyExistsException;
import kr.doridos.dosticket.domain.category.exception.CategoryNotFoundException;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.user.UserType;
import kr.doridos.dosticket.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponse createCategory(final UserType userType, final CategoryRequest categoryRequest) {
        validateUserType(userType);
        validateCategoryName(categoryRequest.getName());

        final Category category = categoryRepository.findById(categoryRequest.getParentId()).orElseThrow(() -> {
                    throw new CategoryNotFoundException(ErrorCode.CATEGORY_NOT_FOUND); });

        final Category createCategory = Category.builder()
                .name(categoryRequest.getName())
                .parent(category)
                .build();
        categoryRepository.save(createCategory);
        return CategoryResponse.of(createCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryListResponse> findAll() {
        List<Category> response = categoryRepository.findAll();
        return response.stream()
                .map(CategoryListResponse::of)
                .collect(Collectors.toList());
    }

    private void validateCategoryName(final String categoryName) {
        if (categoryRepository.existsByName(categoryName))
            throw new CategoryAlreadyExistsException(ErrorCode.CATEGORY_EXIST);
    }

    private void validateUserType(final UserType userType) {
        if (!userType.equals(UserType.TICKET_MANAGER))
            throw new UserNotTicketManagerException(ErrorCode.NOT_TICKET_MANAGER);
    }
}
