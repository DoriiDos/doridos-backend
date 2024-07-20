package kr.doridos.dosticket.domain.ticket.fixture;

import kr.doridos.dosticket.domain.category.entity.Category;

public class CategoryFixture {

    public static Category 카테고리_생성() {
        return Category.builder()
                .id(1L)
                .name("뮤지컬")
                .build();
    }
}
