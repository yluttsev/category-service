package ru.example.categoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.example.categoryservice.model.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Override
    @Query("select distinct c from Category c left join fetch c.children c1 left join fetch c1.children where c.parent is null")
    List<Category> findAll();

    @Override
    @Query("select distinct c from Category c left join fetch c.children c1 left join fetch c1.children where c.id = :id")
    Optional<Category> findById(@Param("id") String id);
}
