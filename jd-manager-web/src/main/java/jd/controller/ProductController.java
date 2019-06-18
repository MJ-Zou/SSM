package jd.controller;

import com.jd.pojo.Product;
import jd.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * 查看商品详情
     *
     * @param pid 商品id
     */
    @RequestMapping("/product_info/{pid}")
    public ModelAndView toProductInfo(@PathVariable String pid, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product_info");

        Product product = productService.getProductById(pid);
        modelAndView.addObject("product", product);

        //向cookie中写入浏览记录
        Cookie[] cookies = request.getCookies();
        boolean flag = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ids")) {
                flag = true;
                String value = cookie.getValue();
                String[] ids = value.split("-");
                LinkedList<String> list = new LinkedList<>(Arrays.asList(ids));

                if (list.contains(pid)) {
                    list.remove(pid);
                }
                if (list.size() > 2) {
                    list.removeLast();
                }
                list.addFirst(pid);

                value = "";
                for (String id : list) {
                    value += "-" + id;
                }
                value = value.substring(1);
                cookie.setValue(value);
                cookie.setPath(request.getContextPath()+"/");
                response.addCookie(cookie);
                break;
            }
        }
        //没有记录则创建记录
        if (!flag) {
            Cookie cookie = new Cookie("ids", pid);
            cookie.setMaxAge(60*10);
            cookie.setPath(request.getContextPath()+"/");
            response.addCookie(cookie);
        }
        return modelAndView;
    }

    /**
     * 分类分页展示商品
     *
     * @param cid 类别id
     * @return
     */
    @RequestMapping("/product_list/{cid}/{page}")
    public ModelAndView toProductList(@PathVariable String cid, @PathVariable Integer page, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("product_list");

        List<Product> products = productService.getProductByCid(cid, page);
        int totalPage = (int) Math.ceil(productService.getProductNumByCid(cid) / 12.);
        modelAndView.addObject("products", products);
        modelAndView.addObject("totalPage", totalPage);
        modelAndView.addObject("curPage", page);

        //读取历史记录
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("ids")) {
                String[] ids = cookie.getValue().split("-");
                List<Product> history = new ArrayList<>();
                for (String pid : ids) {

                    Product product = productService.getProductById(pid);
                    history.add(product);
                }
                modelAndView.addObject("history", history);
                break;
            }
        }

        return modelAndView;
    }
}
