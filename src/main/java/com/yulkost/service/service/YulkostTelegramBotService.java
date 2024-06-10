package com.yulkost.service.service;


import com.yulkost.service.bot.YulkostTelegramBot;
import com.yulkost.service.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YulkostTelegramBotService {
    private final YulkostTelegramBot telegramBot;
    private final ItemsService itemsService;

    public YulkostTelegramBotService(YulkostTelegramBot telegramBot, ItemsService itemsService) {
        this.telegramBot = telegramBot;
        this.itemsService = itemsService;
    }

    public void SendOrderToUser(Orders order){
        try {
            StringBuilder orderInfo = new StringBuilder();
            orderInfo.append("Заказ № ").append(order.getId()).append("\n");
            orderInfo.append("Стол № ").append(order.getNumberOfTable()).append("\n");
            orderInfo.append(infoAboutOrderItems(order.getOrderItems()));
            System.out.println(orderInfo);
            for (User user : order.getShift().getUsers())
                telegramBot.sendMessage(user.getChatId(), orderInfo.toString());
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    private StringBuilder infoAboutOrderItems(List<OrderItems> orderItems){
        StringBuilder orderItemsInfo = new StringBuilder();
        for (OrderItems item :orderItems) {
            orderItemsInfo.append(item.getNameOfItems())
                    .append(" - ").append(item.getQuantity()*Float.parseFloat(item.getUnitPriceToPage()))
                    .append(" ")
                    .append(item.getUnit())
                    .append("\n");
            orderItemsInfo.append(productWeightInfoAboutItem(itemsService.findById(item.getItem()),new StringBuilder()));
        }
        return orderItemsInfo;
    }
    private StringBuilder productWeightInfoAboutItem(Items item,StringBuilder tabulation){
        StringBuilder productWeightInfo = new StringBuilder();
        for (ProductWeight productWeight: item.getProductsWeight()) {
            productWeightInfo
                    .append(tabulation).append("-")
                    .append(productWeight.getProduct().getName())
                    .append("  ").append(productWeight.getWeightToPage())
                    .append("\n");
        }
        tabulation.append("-");
        for (ItemsInItem childItem: item.getChildItems()) {
            productWeightInfo
                    .append(tabulation)
                    .append(childItem.getItem().getNameOfItems())
                    .append(" - ").append(childItem.getQuantity())
                    .append(" ")
                    .append(childItem.getItem().getUnit().getName())
                    .append("\n");
            productWeightInfo.append(productWeightInfoAboutItem(childItem.getItem(),tabulation));
        }
        return productWeightInfo;
    }
}
