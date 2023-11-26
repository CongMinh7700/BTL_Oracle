package com.test.library.repository;

import com.test.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    //  @Query(value = "query",nativeQuery = true)
    Admin findByUserName(String userName);
}
