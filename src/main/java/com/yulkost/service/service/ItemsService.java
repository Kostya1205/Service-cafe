package com.yulkost.service.service;

import com.yulkost.service.model.Items;
import com.yulkost.service.model.ItemsInItem;
import com.yulkost.service.repository.CategoriesRepository;
import com.yulkost.service.repository.ItemsInItemRepository;
import com.yulkost.service.repository.ItemsRepository;
import com.yulkost.service.repository.ProductWeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ItemsService {
    private ItemsRepository itemsRepository;
    private CategoriesRepository categoryRepository;
    private ProductWeightRepository productWeightRepository;
    private ItemsInItemRepository itemsInItemRepository;
    @Autowired
    public void setItemsInItemRepository(ItemsInItemRepository itemsInItemRepository) {
        this.itemsInItemRepository = itemsInItemRepository;
    }

    @Autowired
    public void setProductWeightRepository(ProductWeightRepository productWeightRepository) {
        this.productWeightRepository = productWeightRepository;
    }

    public ItemsService(ItemsRepository itemsRepository, CategoriesRepository categoryRepository) {
        this.itemsRepository = itemsRepository;
        this.categoryRepository = categoryRepository;
    }
    public Iterable<Items> findAll(){
        return itemsRepository.findAll();
    }
    public List<Items> findAllList(){
        return itemsRepository.findAll();
    }

    public Items findByNameOfItemAndPrice(String nameOfItems, int price) {
        return itemsRepository.findByNameOfItemsAndPrice(nameOfItems,price);
    }
    public void saveAll(List<Items> items) {
        for (Items item :
                items) {
            item.setProductsWeight(productWeightRepository.findByItemId(item.getId()));
        }
        itemsRepository.saveAll(items);}

    public void save(Items item) {
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
    public void setChangeTime(Long id){
        Items item = findById(id);
        setChangeTimeToItem(item);
    }
    public void setChangeTimeToItem(Items item){
        if(item==null){
            return;
        }
        item.setDateOfChange(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        itemsRepository.save(item);
        for (ItemsInItem itemInItem :
                itemsInItemRepository.findAllByItemId(item.getId())) {
            setChangeTimeToItem(itemInItem.getParentItem());
        }
    }
    public List<Items> findAllWithoutExist(Items currentItem) {
        List<Items> items = itemsRepository.findAll();
        List<ItemsInItem> itemsInItems = itemsInItemRepository.findAll();
        for (Items item :
                items) {
            if(Objects.equals(item.getId(), currentItem.getId())){
                items.remove(item);
                break;
            }
        }

        for (ItemsInItem itemsInItem :
                itemsInItems) {
            if(Objects.equals(itemsInItem.getItem().getId(), currentItem.getId())){
                items.remove(itemsInItem.getParentItem());
            }
        }
        Iterator<Items> iterator = items.iterator();
        while (iterator.hasNext()) {
            Items item = iterator.next();
            findAllWithoutExistRecurs(item.getChildItems(),currentItem, iterator);
        }
        return items;
    }

    public void findAllWithoutExistRecurs(List<ItemsInItem> itemsInItems,Items currentItem,Iterator<Items> checkedItem) {
        for (ItemsInItem itemInItem :
                itemsInItems) {
            if (itemInItem.getItem()==currentItem){
                checkedItem.remove();
                break;
            }
            findAllWithoutExistRecurs(itemInItem.getItem().getChildItems(),currentItem,checkedItem);
        }
    }

    public void delete(Long id) {
        itemsRepository.deleteById(id);
    }
}
