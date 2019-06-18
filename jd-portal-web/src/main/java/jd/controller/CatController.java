package jd.controller;

import com.jd.pojo.Category;
import jd.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CatController {
    @Autowired
    private CatService catService;

    @RequestMapping("/showCat")
    public ModelAndView showCat() {
        List<Category> categories = catService.allCat();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("list", categories);
        modelAndView.setViewName("/category/list");

        return modelAndView;
    }

    @RequestMapping("/toEditCat/{cid}")
    public ModelAndView toEditCat(@PathVariable String cid) {
        Category cat = catService.getCatByCid(cid);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("cat", cat);
        modelAndView.setViewName("/category/edit");

        return modelAndView;
    }

    @RequestMapping("/editCat")
    public String editCat(Category cat) {
        catService.updateCat(cat);
        return "redirect:/showCat";
    }

    @RequestMapping("/toAddCat")
    public String toAddCat() {
        return "/category/add";
    }

    @RequestMapping("/addCat")
    public String addCat(Category cat) {
        catService.addCat(cat);
        return "redirect:/showCat";
    }

    @RequestMapping("/delCat/{cid}")
    public String delCat(@PathVariable String cid) {
        catService.delCat(cid);
        return "redirect:/showCat";
    }
}
