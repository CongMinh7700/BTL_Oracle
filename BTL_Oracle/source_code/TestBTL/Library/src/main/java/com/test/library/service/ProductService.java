package com.test.library.service;

import com.test.library.dto.ProductDto;
import com.test.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    //For Admin
    List<Product> findAll();
    List<ProductDto> products();
    Product save(MultipartFile imageProduct, ProductDto productDto);
    Product update(MultipartFile imageProduct,ProductDto productDto);
    void deleteById(Long id);
    void enableById(Long id);
    ProductDto getById(Long id);
    Page<ProductDto> pageProducts(int pageNo);
    Page<ProductDto> searchProducts( int pageNo,String keyword);
    List<ProductDto> searchProducts(String keyword);
    //For Customer
    Page<ProductDto> getAllProducts(int pageNo);

    public List<ProductDto> allProduct() ;
    List<ProductDto> randomProduct();

    List<ProductDto> listViewProducts();
    Product getProductById(Long id);
    List<Product> getRelatedProducts(Long categoryId);

    List<ProductDto> findByCategoryId(Long id);

    List<ProductDto> filterHighProducts();
    List<ProductDto> filterLowerProducts();
    Product findById(Long id);
    List<ProductDto> findAllByCategory(String category);
}
