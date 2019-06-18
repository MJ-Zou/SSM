package jd.service;

import com.jd.pojo.Product;

import java.util.List;

public interface ProductService {
    public List<Product> getHotProducts();

    public List<Product> getNewProducts();

    public List<Product> getProductByCid(String cid, Integer page);

    public Product getProductById(String pid);

    public Integer getProductNumByCid(String cid);

    public List<Product> getAllProduct();

    void addProduct(Product product);

    void delProduct(String pid);

    void editProduct(Product product);
}
