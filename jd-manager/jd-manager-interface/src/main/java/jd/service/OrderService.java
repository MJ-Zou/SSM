package jd.service;

import com.jd.pojo.OrderAndProduct;
import com.jd.pojo.Orders;
import com.jd.pojo.PageBean;

import java.util.List;

public interface OrderService {
    public void createOrder(Orders orders, String uid);

    public List<OrderAndProduct> showOrder(String uid, int page);

    public int getOrderNum(String uid);

    public OrderAndProduct orderInfo(String oid);

    PageBean<Orders> allOrder(int state,int page );

    void updateState(String oid);

    void receiveOrder(String oid);

    void payOrder(String oid);
}
