package jd.controller;

import com.jd.pojo.Orderitem;
import com.jd.pojo.Product;
import com.jd.pojo.User;
import jd.service.CartService;
import jd.service.ProductService;
import org.apache.http.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductService productService;

    /**
     * 展示购物车
     *
     * @param request
     * @return
     */
    @RequestMapping("/cart")
    public ModelAndView toCart(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();

        //未登录则跳到登录
        if (user == null) {
            modelAndView.setViewName("/login");
            return modelAndView;
        }
        modelAndView.setViewName("cart");

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

    /**
     * 添加到购物车
     *
     * @param pid
     * @param quantity
     * @param request
     * @return
     */
    @RequestMapping("/add2Cart")
    public String add2Cart(String pid, String quantity, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        //未登录则跳到登录
        if (user == null) {
            return "redirect:/login";
        }

        cartService.add2Cart(pid, Integer.parseInt(quantity), user.getUid());
        return "redirect:/cart";
    }

    /**
     * 清空购物车
     */
    @RequestMapping("/clearCart")
    public String clearCart(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        cartService.clearCart(user.getUid());
        return "redirect:/cart";
    }

    /**
     * 删除某个商品
     */
    @RequestMapping("/remove/{itemid}")
    public String removeFromCart(@PathVariable String itemid) {
        cartService.removeFromCart(itemid);
        return "redirect:/cart";
    }
}
