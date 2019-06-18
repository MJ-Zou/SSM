package jd.controller;

import com.jd.pojo.OrderAndProduct;
import com.jd.pojo.Orders;
import com.jd.pojo.PageBean;
import jd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/allOrder/{state}/{page}")
    public ModelAndView allOrder(@PathVariable String state, @PathVariable String page) {
        PageBean<Orders> pageBean = orderService.allOrder(Integer.parseInt(state), Integer.parseInt(page));

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/order/list");
        modelAndView.addObject("pageBean", pageBean);
        modelAndView.addObject("state", state);

        return modelAndView;
    }

    @RequestMapping("/updateStateOrder/{oid}")
    public String updateStateOrder(@PathVariable String oid) {
        orderService.updateState(oid);
        return "redirect:/allOrder/1/1";
    }

    @RequestMapping("/orderInfo/{oid}")
    @ResponseBody
    public OrderAndProduct orderInfo(@PathVariable String oid) {
        OrderAndProduct orderAndProduct = orderService.orderInfo(oid);
        return orderAndProduct;
    }
}
