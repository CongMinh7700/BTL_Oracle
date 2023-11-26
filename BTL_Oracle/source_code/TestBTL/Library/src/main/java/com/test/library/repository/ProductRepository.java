package com.test.library.repository;

import com.test.library.dto.ProductDto;
import com.test.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("select p from Product p")
    Page<Product > pageProducts(Pageable pageable);
    @Query("select p from Product  p where p.description like %?1% or p.name like %?1% " )
    Page<Product > searchProducts(String keyword,Pageable pageable);
    @Query("select p from Product  p where p.is_activated = true and p.description like %?1% or p.name like %?1%      ")
    List<Product > searchProducts(String keyword);


    //For Customer
    @Query("select p from Product p where p.is_activated = true and p.is_deleted = false")
    List<Product> getAllProducts();
    @Query(value ="SELECT * FROM Products  WHERE is_activated = 1  AND ROWNUM <= 4",nativeQuery = true)
    List<Product> listViewProducts();

    @Query("select p from Product p inner join Category c on c.id = p.category.id where p.category.id = ?1")
    List<Product> getRelatedProducts(Long categoryId);

    @Query("select p from Product p inner join Category c on c.id = p.category.id where p.category.id = ?1")
    List<Product> getProductByCategoryId(Long categoryId);

    @Query("select p from Product p where p.is_deleted = false and   p.is_activated = true order by p.costPrice desc")
    List<Product> filterHighProducts();

    @Query("select p from Product p where p.is_deleted = false and   p.is_activated = true order by p.costPrice asc")
    List<Product> filterLowProducts();
    //new
    @Query("select p from Product p inner join Category c ON c.id = p.category.id" +
            " where p.category.cate_name = ?1 and p.is_activated = true and p.is_deleted = false")
    List<Product> findAllByCategory(String category);

    @Query(value = "select " +
            "p.product_id, p.name, p.description, p.current_quantity, p.cost_price, p.cate_id, p.sale_price, p.image, p.is_activated, p.is_deleted " +
            "from products p where p.is_activated = 1 and p.is_deleted = 0 AND ROWNUM <= 9", nativeQuery = true)
    List<Product> randomProduct();
}
