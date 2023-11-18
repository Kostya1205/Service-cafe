package com.yulkost.service.service;

import com.yulkost.service.model.Categories;
import com.yulkost.service.model.Items;
import com.yulkost.service.model.User;
import com.yulkost.service.repository.CategoriesRepository;
import com.yulkost.service.repository.ItemsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemsService {
    public ItemsRepository itemsRepository;
    public CategoriesRepository categoryRepository;

    public ItemsService(ItemsRepository itemsRepository, CategoriesRepository categoryRepository) {
        this.itemsRepository = itemsRepository;
        this.categoryRepository = categoryRepository;
    }
    public Iterable<Items> findAll(){
        return itemsRepository.findAll();
    }

    public Items findByNameOfItem(String nameOfItems) {
        return itemsRepository.findByNameOfItems(nameOfItems);
    }
    public void saveAll(List<Items> items) {
        itemsRepository.saveAll(itemsRepository.saveAllAndFlush(items));}

    public void save(Items item) {
        item.getCategories().getItems().add(item);
        itemsRepository.save(item);
    }

    public Items findById(Long id) {
        Optional<Items> optionalItems = itemsRepository.findById(id);
        if (optionalItems.isPresent()) {
            return optionalItems.get();
        } else {
            throw new RuntimeException("Объект с id=" + id + " не найден");
        }
    }
}
