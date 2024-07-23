package kr.doridos.dosticket.domain.category.dto;

import kr.doridos.dosticket.domain.category.entity.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryListResponse {

    private Long id;
    private String name;
    private Long parentId;
    private List<CategoryListResponse> children;

    public CategoryListResponse(final Long id, final String name, final Long parentId, final List<CategoryListResponse> children) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.children = children;
    }

    public static CategoryListResponse of(final Category category) {
        return new CategoryListResponse(
                category.getId(),
                category.getName(),
                category.getParent() != null ? category.getParent().getId() : null,
                category.getChildren().stream()
                        .map(CategoryListResponse::of)
                        .collect(Collectors.toList())
        );
    }
}
