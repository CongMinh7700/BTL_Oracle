package com.test.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="categories",uniqueConstraints = @UniqueConstraint(columnNames ="cate_name"))
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_id")
    private Long id;
    private String cate_name;
    private boolean is_deleted;
    private boolean is_activated;

//    public Category(String cate_name){
//        this.cate_name = cate_name;
//        this.is_deleted = false;
//        this.is_activated = true;
//    }
}
