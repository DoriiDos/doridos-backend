package kr.doridos.dosticket.domain.category.service;

import kr.doridos.dosticket.domain.category.dto.CategoryRequest;
import kr.doridos.dosticket.domain.category.dto.CategoryResponse;
import kr.doridos.dosticket.domain.category.entity.Category;
import kr.doridos.dosticket.domain.category.exception.CategoryAlreadyExistsException;
import kr.doridos.dosticket.domain.category.repository.CategoryRepository;
import kr.doridos.dosticket.domain.ticket.exception.UserNotTicketManagerException;
import kr.doridos.dosticket.domain.ticket.fixture.CategoryFixture;
import kr.doridos.dosticket.domain.user.User;
import kr.doridos.dosticket.domain.user.fixture.UserFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    private User ticketManager;
    private User user;
    private Category parentCategory;

    @BeforeEach
    void setUp() {
        ticketManager = UserFixture.관리자_생성();
        parentCategory = CategoryFixture.카테고리_생성();
        user = UserFixture.일반_유저_생성();
    }

    @Test
    void 카테고리_생성에_성공한다() {
        CategoryRequest request = new CategoryRequest("축구", parentCategory.getId());
        given(categoryRepository.findById(parentCategory.getId())).willReturn(Optional.of(parentCategory));

        CategoryResponse response = categoryService.createCategory(ticketManager.getUserType(), request);

        assertSoftly(softly -> {
            softly.assertThat(request.getName()).isEqualTo(response.getName());
            softly.assertThat(request.getParentId()).isEqualTo(response.getParentId());
        });
    }

    @Test
    void 카테고리_생성시_티켓매니저가_아니면_예외가_발생한다() {
        CategoryRequest request = new CategoryRequest("축구", parentCategory.getId());

        assertThatThrownBy(() -> categoryService.createCategory(user.getUserType(), request))
                .isInstanceOf(UserNotTicketManagerException.class)
                .hasMessage("권한이 없는 사용자입니다.");

    }

    @Test
    void 카테고리_생성시_카테고리네임이_이미_존재하면_예외가_발생한다() {
        CategoryRequest request = new CategoryRequest(parentCategory.getName(), parentCategory.getId());
        given(categoryRepository.existsByName(parentCategory.getName())).willReturn(true);

        assertThatThrownBy(() -> categoryService.createCategory(ticketManager.getUserType(), request))
                .isInstanceOf(CategoryAlreadyExistsException.class)
                .hasMessage("카테고리가 이미 존재합니다.");
    }
}