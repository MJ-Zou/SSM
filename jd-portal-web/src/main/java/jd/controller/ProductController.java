package jd.controller;

import com.jd.pojo.Category;
import com.jd.pojo.Product;
import jd.service.CatService;
import jd.service.ProductService;
import jd.utils.UploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CatService catService;

    @RequestMapping("/allProduct")
    public ModelAndView allProduct() {
        List<Product> productList = productService.getAllProduct();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/list");
        modelAndView.addObject("productList", productList);
        return modelAndView;
    }

    @RequestMapping("/toEditProduct/{pid}")
    public ModelAndView toEditProduct(@PathVariable String pid) {
        Product product = productService.getProductById(pid);
        List<Category> cats = catService.allCat();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/edit");
        modelAndView.addObject("product", product);
        modelAndView.addObject("cats", cats);
        return modelAndView;
    }

    @RequestMapping("/editProduct")
    public String editProduct(Product product, @RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (!filename.trim().isEmpty()) {
            String uuidName = UploadUtils.getUUIDName(filename);
            String fileLoc = "C:\\upload\\";
            File newFile = new File(fileLoc + uuidName);
            file.transferTo(newFile);
            product.setPimage("upload/" + uuidName);
        }
        productService.editProduct(product);

        return "redirect:/allProduct";
    }

    @RequestMapping("/toAddProduct")
    public ModelAndView toAddProduct() {

        List<Category> cats = catService.allCat();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/product/add");
        modelAndView.addObject("cats", cats);
        return modelAndView;
    }

    @RequestMapping("/addProduct")
    public String addProduct(Product product, @RequestParam("file") MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        System.out.println(filename);
        if (!filename.trim().isEmpty()) {
            String uuidName = UploadUtils.getUUIDName(filename);
            String fileLoc = "C:\\upload\\";
            File newFile = new File(fileLoc + uuidName);
            file.transferTo(newFile);
            product.setPimage("upload/" + uuidName);
        }

        productService.addProduct(product);
        return "redirect:/allProduct";
    }

    @RequestMapping("/delProduct/{pid}")
    public String addProduct(@PathVariable String pid) {
        productService.delProduct(pid);
        return "redirect:/allProduct";
    }
}
