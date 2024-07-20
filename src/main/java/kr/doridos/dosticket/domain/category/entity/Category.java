package kr.doridos.dosticket.domain.category.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }
}
