package jd.service.impl;

import com.jd.mapper.CategoryMapper;
import com.jd.mapper.ProductMapper;
import com.jd.pojo.Category;
import com.jd.pojo.CategoryExample;
import com.jd.pojo.Product;
import com.jd.pojo.ProductExample;
import jd.service.CatService;
import jd.service.JedisClient;
import jd.utils.JsonUtils;

import jd.utils.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatServiceImpl implements CatService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private JedisClient jedisClient;
    @Autowired
    private ProductMapper productMapper;

    @Value("${INDEX_CONTEXT}")
    private String INDEX_CONTEXT;

    @Override
    public List<Category> allCat() {
        //先查询缓存
        //不能影响正常业务
        try {
            //查询缓存
            String json = jedisClient.hget(INDEX_CONTEXT, INDEX_CONTEXT);
            //查询到，转换成list
            if (StringUtils.isNoneBlank(json)) {
                List<Category> categories = JsonUtils.jsonToList(json, Category.class);
//                System.out.println("使用redis");
                return categories;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //缓存没有再查询数据库
        List<Category> categories = categoryMapper.selectByExample(new CategoryExample());

        //结果添加到缓存
        try {
            jedisClient.hset(INDEX_CONTEXT, INDEX_CONTEXT, JsonUtils.objectToJson(categories));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //返回
        return categories;
    }


    @Override
    public Category getCatByCid(String cid) {
        return categoryMapper.selectByPrimaryKey(cid);
    }

    @Override
    public void updateCat(Category cat) {
        String cid = cat.getCid();
        Category category = categoryMapper.selectByPrimaryKey(cid);
        category.setCname(cat.getCname());
        categoryMapper.updateByPrimaryKey(category);

        //删除缓存信息
        jedisClient.hdel(INDEX_CONTEXT, INDEX_CONTEXT);
    }


    @Override
    public void addCat(Category category) {
        category.setCid(UUIDUtils.getId());
        categoryMapper.insert(category);

        //同步缓存信息
        //删除缓存信息
        jedisClient.hdel(INDEX_CONTEXT, INDEX_CONTEXT);
    }

    @Override
    public void delCat(String cid) {
        //！！！将该类目下商品分类变成空！！！
        ProductExample example=new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        List<Product> productList = productMapper.selectByExample(example);
        for (Product product:productList){
            product.setCid(null);
            productMapper.updateByPrimaryKey(product);
        }

        categoryMapper.deleteByPrimaryKey(cid);
        //删除缓存信息
        jedisClient.hdel(INDEX_CONTEXT, INDEX_CONTEXT);
    }
}
