package jd.service.impl;

import com.jd.mapper.UserMapper;
import com.jd.pojo.PageBean;
import com.jd.pojo.User;
import com.jd.pojo.UserExample;
import jd.service.UserService;
import jd.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> userLogin(User user) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(user.getUsername());
        criteria.andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(example);
        return users;
    }

    @Override
    public int userRegister(User user) {
        user.setState(0);
        user.setUid(UUIDUtils.getId());
        user.setCode(UUIDUtils.getCode());
        int insert = userMapper.insert(user);
        return insert;
    }

    @Override
    public PageBean<User> allUser(int page) {
        UserExample example = new UserExample();
        example.setStart((page - 1) * 3);
        example.setNum(3);
        List<User> users = userMapper.selectByExample(example);
        int total = userMapper.countByExample(new UserExample());

        PageBean<User> pageBean = new PageBean<>();
        pageBean.setList(users);
        pageBean.setPage(page);
        pageBean.setTotalPage((int) Math.ceil(total / 3.));

        return pageBean;
    }
}
