package data;

import java.util.ArrayList;

public class Order {

    String id;
    String action;
    String side;
    int amount;
    String status;
    String price;
    String orderTag;

    public Order(String orderTag, String action, String side, int amount, String status, String price, String id) {
        this.orderTag = orderTag;
        this.action = action;
        this.side = side;
        this.amount = amount;
        this.status = status;
        this.price = price;
        this.id = id;
    }

    public String getOrderTag() {
        return orderTag;
    }

    public void setOrderTag(String orderTag) {
        this.orderTag = orderTag;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
