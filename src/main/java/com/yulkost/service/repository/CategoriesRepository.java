package com.yulkost.service.repository;

import com.yulkost.service.model.Categories;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository <Categories,Long> {
    List<Categories> findAllByParentCategory(Categories parentCategory,Sort sort);
    List<Categories> findAll(Sort sort);
}
