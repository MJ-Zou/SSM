package jd.service.impl;


import com.jd.mapper.OrderitemMapper;
import com.jd.mapper.ProductMapper;
import com.jd.pojo.Orderitem;
import com.jd.pojo.OrderitemExample;
import com.jd.pojo.Product;
import jd.service.CartService;
import jd.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private OrderitemMapper orderitemMapper;
    @Autowired
    private ProductMapper productMapper;

    /**
     * 加入购物车
     *
     * @param pid
     * @param num
     */
    @Override
    public void add2Cart(String pid, int num, String uid) {
        Product product = productMapper.selectByPrimaryKey(pid);
        Double shopPrice = product.getShopPrice();

        //先看是否购物车已经包含该商品
        OrderitemExample example = new OrderitemExample();
        OrderitemExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOidIsNull();
        List<Orderitem> orderitems = orderitemMapper.selectByExample(example);
        for (Orderitem order : orderitems) {
            //包含，数量增加，金额增加
            if (order.getPid().equals(pid)) {
                Integer count = order.getCount();
                order.setCount(count + num);
                Double subtotal = order.getSubtotal();
                order.setSubtotal(subtotal + num * shopPrice);

                orderitemMapper.updateByPrimaryKey(order);
                return;
            }
        }

        //不包含,新建
        Orderitem order = new Orderitem();
        order.setItemid(UUIDUtils.getId());
        order.setCount(num);
        order.setSubtotal(num * shopPrice);
        order.setPid(pid);
        order.setUid(uid);
        orderitemMapper.insert(order);
    }

    /**
     * 删除某个商品
     *
     */
    @Override
    public void removeFromCart(String itemid) {
        OrderitemExample example = new OrderitemExample();
        OrderitemExample.Criteria criteria = example.createCriteria();
        criteria.andItemidEqualTo(itemid);
        orderitemMapper.deleteByExample(example);
    }

    /**
     * 清空购物车
     *
     * @param uid
     */
    @Override
    public void clearCart(String uid) {
        OrderitemExample example = new OrderitemExample();
        OrderitemExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOidIsNull();
        orderitemMapper.deleteByExample(example);
    }

    /**
     * 展示购物车
     *
     * @param uid
     * @return
     */
    @Override
    public List<Orderitem> showCart(String uid) {
        OrderitemExample example = new OrderitemExample();
        OrderitemExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOidIsNull();
        return orderitemMapper.selectByExample(example);
    }


}
