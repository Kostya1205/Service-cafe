package com.yulkost.service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;

@Data
@Entity
public class ProductStockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int weight;
    private int priceOnStock;
    private int priceMovement;

    private int balanceWeight;

    private String description;
    /**
     * true - adding false - care
     */
    private String typeOfOperation;
    private LocalDateTime dateOfOperation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_items_id", referencedColumnName = "id")
    private OrderItems orderItems;
    private Long product;
    private String productName;
    public String getPriceMovementToPage() {
        double pr = priceMovement;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("0.00",symbols).format(pr/100);
    }
    public void setPriceMovementToPage(String price) {
        this.priceMovement = (int)(Double.parseDouble(price)*100);;
    }
    public String getPriceOnStockToPage() {
        double pr = priceOnStock;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("0.00",symbols).format(pr/100);
    }
    public void setPriceOnStockToPage(String price) {
        this.priceOnStock = (int)(Double.parseDouble(price)*100);;
    }
    public String getWeightToPage() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("0.000",symbols).format(((float)this.weight) / 1000);
    }
    public String getBalanceWeightToPage() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        return new DecimalFormat("0.000",symbols).format(((float)this.balanceWeight) / 1000);
    }
    public void setWeightToPage(String weight) {
        this.weight=(int)(Double.parseDouble(weight)*1000);

    }
    public String getSumToPage() {
        return new DecimalFormat("0.00").format(((float)this.weight)/1000 *((float)this.priceMovement)/100);
    }
}
