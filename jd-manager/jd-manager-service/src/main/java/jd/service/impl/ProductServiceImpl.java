package jd.service.impl;

import com.jd.mapper.ProductMapper;
import com.jd.pojo.Product;
import com.jd.pojo.ProductExample;
import jd.service.ProductService;
import jd.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductMapper productMapper;

    /**
     * 获得9个热门商品
     *
     * @return
     */
    @Override
    public List<Product> getHotProducts() {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andIsHotEqualTo(1);
        example.setNum(9);

        List<Product> products = productMapper.selectByExample(example);
        return products;
    }

    /**
     * 获得9个最新商品
     *
     * @return
     */
    @Override
    public List<Product> getNewProducts() {
        ProductExample example = new ProductExample();
        example.setOrderByClause(" pdate desc");
        example.setNum(9);

        List<Product> products = productMapper.selectByExample(example);
        return products;
    }

    /**
     * 分类分页展示商品
     *
     * @param cid
     * @return
     */
    @Override
    public List<Product> getProductByCid(String cid, Integer page) {
        ProductExample example = new ProductExample();

        example.setNum(12);
        example.setStart((page - 1) * 12);

        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andCidEqualTo(cid);

        List<Product> products = productMapper.selectByExample(example);
        return products;
    }

    /**
     * 通过主键查询商品
     *
     * @param pid
     * @return
     */
    @Override
    public Product getProductById(String pid) {

        return productMapper.selectByPrimaryKey(pid);
    }

    /**
     * 分类商品下的数量
     *
     * @param cid
     * @return
     */
    @Override
    public Integer getProductNumByCid(String cid) {
        ProductExample example = new ProductExample();
        ProductExample.Criteria criteria = example.createCriteria();
        criteria.andCidEqualTo(cid);

        return productMapper.countByExample(example);
    }

    /**
     * 查出所有商品 暂不分页
     *
     * @return
     */
    @Override
    public List<Product> getAllProduct() {
        ProductExample example = new ProductExample();
        example.setOrderByClause(" pdate desc");
        return productMapper.selectByExample(example);
    }

    /**
     * 添加商品
     *
     * @param product
     */
    @Override
    public void addProduct(Product product) {
        product.setPdate(new Date());
        product.setPflag(1);
        product.setPid(UUIDUtils.getId());
        productMapper.insert(product);
    }

    @Override
    public void delProduct(String pid) {
        productMapper.deleteByPrimaryKey(pid);
    }

    @Override
    public void editProduct(Product product) {
        Product p = productMapper.selectByPrimaryKey(product.getPid());
        p.setPname(product.getPname());
        p.setIsHot(product.getIsHot());
        p.setMarketPrice(product.getMarketPrice());
        p.setShopPrice(product.getShopPrice());
        p.setCid(product.getCid());
        p.setPdesc(product.getPdesc());
        if (product.getPimage() != null) {
            p.setPimage(product.getPimage());
        }
        productMapper.updateByPrimaryKey(p);
    }
}
