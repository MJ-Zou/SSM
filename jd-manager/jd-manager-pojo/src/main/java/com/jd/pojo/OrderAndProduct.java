package com.jd.pojo;

import java.io.Serializable;
import java.util.List;

public class OrderAndProduct implements Serializable {
    private List<Product> productList;
    private List<Orderitem> orderitems;
    private Orders orders;

    public List<Orderitem> getOrderitems() {
        return orderitems;
    }

    public void setOrderitems(List<Orderitem> orderitems) {
        this.orderitems = orderitems;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }
}
