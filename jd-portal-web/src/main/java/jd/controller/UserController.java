package jd.controller;

import com.jd.pojo.PageBean;
import com.jd.pojo.User;
import jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/allUser/{page}")
    public ModelAndView allUser(@PathVariable String page){
        PageBean<User> pageBean = userService.allUser(Integer.parseInt(page));

        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("pageBean",pageBean);
        modelAndView.setViewName("/user/list");

        return modelAndView;
    }

}
