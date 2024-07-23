package kr.doridos.dosticket.domain.category.repository;

import kr.doridos.dosticket.domain.category.entity.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(final Long id);

    boolean existsByName(final String categoryName);

    @EntityGraph(attributePaths = "children")
    @Query("select c from Category c where c.parent is null")
    List<Category> findAll();
}

