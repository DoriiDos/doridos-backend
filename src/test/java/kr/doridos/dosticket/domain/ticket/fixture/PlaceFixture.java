package kr.doridos.dosticket.domain.ticket.fixture;

import kr.doridos.dosticket.domain.place.entity.Place;

public class PlaceFixture {

    public static Place 장소_생성() {
        return Place.builder()
                .id(1L)
                .name("잠실")
                .location("서울특별시")
                .build();
    }
}
