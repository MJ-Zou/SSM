package jd.service.impl;

import com.jd.mapper.OrderitemMapper;
import com.jd.mapper.OrdersMapper;
import com.jd.mapper.ProductMapper;
import com.jd.pojo.*;
import jd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderitemMapper orderitemMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public void createOrder(Orders orders, String uid) {
        String oid = orders.getOid();

        //验证订单是否已存在
        Orders order = ordersMapper.selectByPrimaryKey(oid);
        if (order != null) {
            order.setAddress(orders.getAddress());
            order.setName(orders.getName());
            order.setTelephone(orders.getTelephone());
            ordersMapper.updateByPrimaryKey(order);
            return;
        }

        //创建订单
        orders.setOrdertime(new Date());
        orders.setState(0);//0-未支付 1-已支付
        orders.setUid(uid);
        ordersMapper.insert(orders);

        //购物车商品加到订单下
        OrderitemExample example = new OrderitemExample();
        OrderitemExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        criteria.andOidIsNull();
        List<Orderitem> orderitems = orderitemMapper.selectByExample(example);
        for (Orderitem item : orderitems) {
            item.setOid(oid);
            orderitemMapper.updateByPrimaryKey(item);
        }
    }

    /**
     * 分页展示订单
     *
     * @param uid
     * @return
     */
    @Override
    public List<OrderAndProduct> showOrder(String uid, int page) {
        List<OrderAndProduct> orderAndProductList = new ArrayList<>();

        OrdersExample example = new OrdersExample();
        example.setNum(3);
        example.setStart((page - 1) * 3);
        example.createCriteria().andUidEqualTo(uid);
        List<Orders> orders = ordersMapper.selectByExample(example);
        for (Orders order : orders) {
            OrderAndProduct orderAndProduct = new OrderAndProduct();
            //加入订单信息
            orderAndProduct.setOrders(order);

            //加入商品数量总价
            OrderitemExample iexample = new OrderitemExample();
            iexample.createCriteria().andOidEqualTo(order.getOid());
            List<Orderitem> orderitems = orderitemMapper.selectByExample(iexample);
            orderAndProduct.setOrderitems(orderitems);

            //添加商品图片名称
            List<Product> products = new ArrayList<>();
            for (Orderitem orderitem : orderitems) {
                Product product = productMapper.selectByPrimaryKey(orderitem.getPid());
                products.add(product);
            }
            orderAndProduct.setProductList(products);

            orderAndProductList.add(orderAndProduct);
        }
        return orderAndProductList;
    }

    @Override
    public int getOrderNum(String uid) {
        OrdersExample example = new OrdersExample();
        example.createCriteria().andUidEqualTo(uid);
        return ordersMapper.countByExample(example);
    }

    @Override
    public OrderAndProduct orderInfo(String oid) {
        OrderAndProduct orderAndProduct = new OrderAndProduct();

        //加入订单信息
        Orders order = ordersMapper.selectByPrimaryKey(oid);
        orderAndProduct.setOrders(order);

        //加入商品数量总价
        OrderitemExample iexample = new OrderitemExample();
        iexample.createCriteria().andOidEqualTo(oid);
        List<Orderitem> orderitems = orderitemMapper.selectByExample(iexample);
        orderAndProduct.setOrderitems(orderitems);

        //添加商品图片名称
        List<Product> products = new ArrayList<>();
        for (Orderitem orderitem : orderitems) {
            Product product = productMapper.selectByPrimaryKey(orderitem.getPid());
            products.add(product);
        }
        orderAndProduct.setProductList(products);

        return orderAndProduct;
    }

    /**
     * 根据订单状态分页展示
     * @param state
     * @param page
     * @return
     */
    @Override
    public PageBean<Orders> allOrder(int state, int page) {
        OrdersExample example = new OrdersExample();
        if (state != -1) {
            example.createCriteria().andStateEqualTo(state);
        }
        int count = ordersMapper.countByExample(example);

        example.setNum(3);
        example.setStart((page - 1) * 3);
        List<Orders> orders = ordersMapper.selectByExample(example);

        PageBean<Orders> pageBean = new PageBean<>();
        pageBean.setList(orders);
        pageBean.setTotalPage((int) Math.ceil(count / 3.));
        pageBean.setPage(page);

        return pageBean;
    }

    /**
     * 订单发货
     * @param oid
     */
    @Override
    public void updateState(String oid) {
        Orders orders = ordersMapper.selectByPrimaryKey(oid);
        orders.setState(2);
        ordersMapper.updateByPrimaryKey(orders);
    }

    /**
     * 确认收货
     * @param oid
     */
    @Override
    public void receiveOrder(String oid) {
        Orders orders = ordersMapper.selectByPrimaryKey(oid);
        orders.setState(3);
        ordersMapper.updateByPrimaryKey(orders);
    }

    /**
     * 支付订单
     * @param oid
     */
    @Override
    public void payOrder(String oid) {
        Orders orders = ordersMapper.selectByPrimaryKey(oid);
        orders.setState(1);
        ordersMapper.updateByPrimaryKey(orders);
    }


}
