package kr.doridos.dosticket.domain.ticket.fixture;

import kr.doridos.dosticket.domain.category.entity.Category;

public class CategoryFixture {

    public static Category 카테고리_생성() {
        return Category.builder()
                .id(1L)
                .name("뮤지컬")
                .build();
    }

    public static Category 하위_카테고리_생성() {
        return Category.builder()
                .id(2L)
                .name("오페라")
                .parent(CategoryFixture.카테고리_생성())
                .build();
    }
}
