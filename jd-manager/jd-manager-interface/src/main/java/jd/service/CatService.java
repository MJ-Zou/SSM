package jd.service;

import com.jd.pojo.Category;

import java.util.List;

public interface CatService {
    public List<Category> allCat();

    public void addCat(Category category);

    Category getCatByCid(String cid);

    void updateCat(Category cat);

    void delCat(String cid);
}

