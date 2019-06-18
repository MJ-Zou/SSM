package jd.service;


import com.jd.pojo.PageBean;
import com.jd.pojo.User;

import java.util.List;

public interface UserService {
    List<User> userLogin(User user);

    int userRegister(User user);

    PageBean<User> allUser(int page);
}
