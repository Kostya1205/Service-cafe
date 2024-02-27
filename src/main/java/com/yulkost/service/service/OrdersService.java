package com.yulkost.service.service;

import com.yulkost.service.model.CashRegister;
import com.yulkost.service.model.Items;
import com.yulkost.service.model.OrderItems;
import com.yulkost.service.model.Orders;
import com.yulkost.service.repository.CashRegisterRepository;
import com.yulkost.service.repository.OrderItemsRepository;
import com.yulkost.service.repository.OrdersRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {
    public OrdersRepository ordersRepository;
    public ItemsService itemsService;
    public OrderItemsRepository orderItemsRepository;
    private CashRegisterRepository cashRegisterRepository;
    private ProductStockService productStockService;

    public OrdersService(OrdersRepository ordersRepository, ItemsService itemsService, OrderItemsRepository orderItemsRepository, CashRegisterRepository cashRegisterRepository, ProductStockService productStockService) {
        this.ordersRepository = ordersRepository;
        this.itemsService = itemsService;
        this.orderItemsRepository = orderItemsRepository;
        this.cashRegisterRepository = cashRegisterRepository;
        this.productStockService = productStockService;
    }

    public Orders OrderFromPageToOrders(Orders order){
        if(order.getOrderItems().isEmpty())
            return null;
        Orders newOrder = new Orders();
        newOrder.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        newOrder.setShift(order.getShift());
        newOrder.setCashPaid(order.getCashPaid());
        newOrder.setCashLessPaid(order.getCashLessPaid());
        newOrder.setEstablishmentPaid(order.getEstablishmentPaid());
        newOrder.setSumOfChange(order.getSumOfChange());
        newOrder.setNumberOfTable(order.getNumberOfTable());
        List<OrderItems> orderItems = new ArrayList<>();

        for (OrderItems orderItem1: order.getOrderItems())
        {
            Items item = itemsService.findById(orderItem1.getItem());
            OrderItems orderItem = new OrderItems();
            orderItem.setQuantity(orderItem1.getQuantity());
            orderItem.setPrice(orderItem1.getPrice());
            orderItem.setDateOfItemChange(item.getDateOfChange());
            orderItem.setNameOfItems(item.getNameOfItems());
            orderItem.setItem(item.getId());
            orderItem.setTypeOfItem(item.getTypeOfItem());
            orderItem.setCategory(item.getCategories().getCategoriesName());
            orderItem.setUniqueCode(item.getUniqueCode());
            orderItem.setUnit(item.getUnit().getName());
            orderItem.setUnitPrice(item.getUnitPrice());
            orderItems.add(orderItem);
        }
        newOrder.setOrderItems(orderItems);
        return newOrder;
    }
        public void save(Orders order){
            orderItemsRepository.saveAll(order.getOrderItems());
            Orders orders = ordersRepository.save(order);
        if (order.getEstablishmentPaid()<=0){
            CashRegister cashRegister = new CashRegister();
            CashRegister cashRegister1 = cashRegisterRepository.findCashRegisterWithMaxId();
            if(cashRegister1==null){
                cashRegister.setCashAmount(order.getCashPaid());
            }else{
                cashRegister.setCashAmount(cashRegister1.getCashAmount()+order.getCashPaid());
            }
            cashRegister.setOrder(orders);
            cashRegisterRepository.save(cashRegister);
        }
            productStockService.writeOffProductFromStockAndSaveToStockMovement(order);
    }
}
