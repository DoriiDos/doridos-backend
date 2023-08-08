package kr.doridos.dosticket.domain.category.repository;

import kr.doridos.dosticket.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findById(final Long id);
}
