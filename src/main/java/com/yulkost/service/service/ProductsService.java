package com.yulkost.service.service;

import com.yulkost.service.model.ProductStock;
import com.yulkost.service.model.Products;
import com.yulkost.service.repository.ProductStockRepository;
import com.yulkost.service.repository.ProductsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductsService {
    private ProductStockRepository productStockRepository;
    private ProductsRepository productsRepository;
    @Autowired
    public void setProductStockRepository(ProductStockRepository productStockRepository) {
        this.productStockRepository = productStockRepository;
    }
    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Iterable<Products> findAll() {
        return productsRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public void saveAll(List<Products> products) {
        productsRepository.saveAll(products);
    }

    public void save(Products product) {
        productsRepository.save(product);
        if(!productStockRepository.existsByProductId(product.getId())){
            // Если не существует, создаем новую запись в ProductStock
            ProductStock productStock = new ProductStock();
            productStock.setProduct(product);
            productStock.setWeight(0);
            productStock.setPrice(0);
            productStockRepository.save(productStock);

            // Обновляем продукт с ссылкой на ProductStock
            product.setProductStock(productStock);
            productsRepository.save(product);
        }
    }

    public Products findById(Long id) {
        Optional<Products> optionalProducts = productsRepository.findById(id);
        if (optionalProducts.isPresent()) {
            return optionalProducts.get();
        } else {
            throw new RuntimeException("Объект с id=" + id + " не найден");
        }
    }
}
