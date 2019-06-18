package jd.controller;

import com.jd.pojo.Category;
import com.jd.pojo.Product;
import jd.service.CatService;
import jd.service.JedisClient;
import jd.service.ProductService;
import jd.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 页面展示
 */
@Controller
public class PageController {
    @Autowired
    private CatService catService;
    @Autowired
    private ProductService productService;


    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }

    /**
     * 首页展示
     */
    @RequestMapping("/index")
    public ModelAndView toIndex() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        //获取热门商品
        List<Product> hotProducts = productService.getHotProducts();
        modelAndView.addObject("hotList", hotProducts);
        //获取最新商品
        List<Product> newProducts = productService.getNewProducts();
        modelAndView.addObject("newList", newProducts);

        return modelAndView;
    }

    /**
     * Ajax获取页面头部分类数据
     */
    @RequestMapping("/getCat")
    @ResponseBody
    public List<Category> getCat() {
        return catService.allCat();
    }

    @RequestMapping("/toLogin")
    public ModelAndView toLogin(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("username")) {
                modelAndView.addObject("username", cookie.getValue());
            }
            if (cookie.getName().equals("pwd")) {
                modelAndView.addObject("pwd", cookie.getValue());
            }
        }
        return modelAndView;
    }

}
