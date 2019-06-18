package jd.controller;

import com.jd.pojo.User;
import jd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public String userLogin(User user, String rememberUser, String autoLog,
                            HttpServletRequest request, HttpServletResponse response) {
        //记住用户名和密码
        if (rememberUser.equals("1")) {
            Cookie cookie1 = new Cookie("username", user.getUsername());
            Cookie cookie2 = new Cookie("pwd", user.getPassword());
            cookie1.setPath(request.getContextPath() + "/");
            cookie2.setPath(request.getContextPath() + "/");
            response.addCookie(cookie1);
            response.addCookie(cookie2);
        }

        List<User> users = userService.userLogin(user);
        if (users.size() > 0) {


            HttpSession session = request.getSession();
            session.setAttribute("user", users.get(0));
            session.removeAttribute("err");
            return "redirect:/index";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("err", 1);
            return "redirect:/login";
        }
    }

    @RequestMapping("quit")
    public String userQuit(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return "redirect:/index";
    }

    @RequestMapping("register")
    public ModelAndView userRegister(User user) {
        userService.userRegister(user);

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("msg", "注册成功");
        modelAndView.setViewName("msg");

        return modelAndView;
    }
}
