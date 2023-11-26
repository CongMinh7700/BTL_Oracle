package com.test.library.service;

import com.test.library.dto.AdminDto;
import com.test.library.model.Admin;

public interface AdminService {
    Admin findByUserName(String userName);
    Admin save(AdminDto adminDto);
}
