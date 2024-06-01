package com.yulkost.service.service;

import com.yulkost.service.dto.CategoriesDtoToItemsEdit;
import com.yulkost.service.dto.CategoriesDtoToMenu;
import com.yulkost.service.dto.CategoriesDtoToPage;
import com.yulkost.service.dto.mapper.CategoriesDtoToItemsEditMapper;
import com.yulkost.service.dto.mapper.CategoriesDtoToMenuMapper;
import com.yulkost.service.dto.mapper.CategoriesDtoToPageMapper;
import com.yulkost.service.model.Categories;
import com.yulkost.service.repository.CategoriesRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoriesDtoToPageMapper categoriesDtoToPageMapper;
    private final CategoriesDtoToItemsEditMapper categoriesDtoToItemsEditMapper;
    private final CategoriesDtoToMenuMapper categoriesDtoToMenuMapper;

    public CategoriesService(CategoriesRepository categoriesRepository, CategoriesDtoToPageMapper categoriesDtoToPageMapper, CategoriesDtoToItemsEditMapper categoriesDtoToItemsEditMapper, CategoriesDtoToMenuMapper categoriesDtoToMenuMapper) {
        this.categoriesRepository = categoriesRepository;
        this.categoriesDtoToPageMapper = categoriesDtoToPageMapper;
        this.categoriesDtoToItemsEditMapper = categoriesDtoToItemsEditMapper;
        this.categoriesDtoToMenuMapper = categoriesDtoToMenuMapper;
    }

    public void save(Categories categories){
        categoriesRepository.save(categories);
}
    public List<CategoriesDtoToPage> findAll(){

        return categoriesRepository.findAll(Sort.by(Sort.Direction.ASC, "categoriesName"))
                .stream()
                .map(categoriesDtoToPageMapper)
        .collect(Collectors.toList());
    }
    public List<CategoriesDtoToMenu> findAllForMenu(){

        return categoriesRepository.findAllByParentCategory(null,Sort.by(Sort.Direction.ASC, "categoriesName"))
                .stream()
                .map(categoriesDtoToMenuMapper)
        .collect(Collectors.toList());
    }
    public List<CategoriesDtoToItemsEdit> findAllToItemsEdit(){

        return categoriesRepository.findAll(Sort.by(Sort.Direction.ASC, "categoriesName"))
                .stream()
                .map(categoriesDtoToItemsEditMapper)
                .collect(Collectors.toList());
    }
    public List<Categories> findAllCategories(){

        return categoriesRepository.findAll(Sort.by(Sort.Direction.ASC, "categoriesName"));
    }

    public void saveAll(List<Categories> categories) {
        categoriesRepository.saveAll(categories);
    }

    public void deleteById(Long id) {
        categoriesRepository.deleteById(id);
    }
}
