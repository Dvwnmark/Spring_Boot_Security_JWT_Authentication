package com.springjwtapplication.repositories;

import com.springjwtapplication.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
