package com.test.library.repository;

import com.test.library.dto.CategoryDto;
import com.test.library.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("select c from Category c where c.is_activated = true and c.is_deleted = false")
    List<Category> findAllByActivated();

    @Query(value = "update Category set cate_name = ?1 where id = ?2")
    Category update(String name, Long id);

    //Customer
    @Query("select new com.test.library.dto.CategoryDto(c.id,c.cate_name,count(p.category.id)) from Category c inner join Product p on c.id = p.category.id   where c.is_activated = true and c.is_deleted = false group by c.id , c.cate_name")
    List<CategoryDto> getCategoriesAndSize();
}
