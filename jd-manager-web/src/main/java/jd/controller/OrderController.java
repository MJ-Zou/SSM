package jd.controller;

import com.jd.pojo.*;
import jd.service.CartService;
import jd.service.OrderService;
import jd.service.ProductService;
import jd.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    CartService cartService;
    @Autowired
    OrderService orderService;
    @Autowired
    ProductService productService;

    @RequestMapping("/to_order")
    public ModelAndView toOrderInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView();
        //未登录则跳到登录
        if (user == null) {
            modelAndView.setViewName("/login");
            return modelAndView;
        }
        modelAndView.setViewName("order_info");

        //订单id
        String oid = UUIDUtils.getCode();
        modelAndView.addObject("oid", oid);

        //订单信息添加
        List<Orderitem> orderitems = cartService.showCart(user.getUid());
        modelAndView.addObject("orderitems", orderitems);

        //商品信息添加，总金额计算
        List<Product> productList = new ArrayList<>();
        double total = 0;
        for (Orderitem orderitem : orderitems) {
            Product product = productService.getProductById(orderitem.getPid());
            productList.add(product);
            total += orderitem.getSubtotal();
        }
        modelAndView.addObject("productList", productList);
        modelAndView.addObject("total", total);

        return modelAndView;
    }

    @RequestMapping("/createOrder")
    public String createOrder(Orders orders, String oid, HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        orders.setOid(oid);
        orderService.createOrder(orders, user.getUid());
        return "redirect:/order_list/1";
    }

    @RequestMapping("/order_list/{page}")
    public ModelAndView order_list(HttpServletRequest request, @PathVariable String page) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        ModelAndView modelAndView = new ModelAndView();
        //未登录则跳到登录
        if (user == null) {
            modelAndView.setViewName("/login");
            return modelAndView;
        }
        modelAndView.setViewName("order_list");

        //展示订单信息
        List<OrderAndProduct> ordersList = orderService.showOrder(user.getUid(), Integer.parseInt(page));
        modelAndView.addObject("orderList", ordersList);

        //分页信息展示
        int num = orderService.getOrderNum(user.getUid());
        int totalPage = (int) Math.ceil(num / 3.);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("curPage", page);

        return modelAndView;
    }

    /**
     * 查看订单
     *
     * @param oid
     * @return
     */
    @RequestMapping("/order_info/{oid}")
    public ModelAndView order_info(@PathVariable String oid) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("order_info");

        OrderAndProduct orderAndProduct = orderService.orderInfo(oid);
        modelAndView.addObject("orderitems", orderAndProduct.getOrderitems());
        modelAndView.addObject("productList", orderAndProduct.getProductList());
        modelAndView.addObject("order", orderAndProduct.getOrders());
        modelAndView.addObject("total", orderAndProduct.getOrders().getTotal());

        return modelAndView;
    }

    @RequestMapping("/receiveOrder/{oid}")
    public String receiveOrder(@PathVariable String oid) {
        orderService.receiveOrder(oid);
        return "redirect:/order_list/1";
    }

    @RequestMapping("/payOrder/{oid}")
    public String payOrder(@PathVariable String oid) {
        orderService.payOrder(oid);
        return "redirect:/order_list/1";
    }
}
