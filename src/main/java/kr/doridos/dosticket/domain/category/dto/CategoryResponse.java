package kr.doridos.dosticket.domain.category.dto;

import kr.doridos.dosticket.domain.category.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryResponse {

    private Long id;
    private String name;
    private Long parentId;

    public CategoryResponse(final Long id, final String name, final Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public static CategoryResponse of(final Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getParent().getId()
        );
    }
}
