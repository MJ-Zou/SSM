package jd.service;

import com.jd.pojo.Orderitem;

import java.util.List;

public interface CartService {
    public void add2Cart(String pid, int num,String uid);

    public void removeFromCart(String itemid);

    public void clearCart(String uid);

    public List<Orderitem> showCart(String uid);
}
