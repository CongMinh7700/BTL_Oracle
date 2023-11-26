package com.test.library.service.impl;

import com.test.library.dto.AdminDto;
import com.test.library.model.Admin;
import com.test.library.repository.AdminRepository;
import com.test.library.repository.RoleRepository;
import com.test.library.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public Admin findByUserName(String userName){
        return  adminRepository.findByUserName(userName);
    }
    @Override
    public Admin save(AdminDto adminDto){
        Admin admin =new Admin();
        admin.setFirstName(adminDto.getFirstName());
        admin.setLastName(adminDto.getLastName());
        admin.setUserName(adminDto.getUserName());
        admin.setPassword(adminDto.getPassword());
        admin.setRoles(Arrays.asList(roleRepository.findByName("ADMIN")));
        return  adminRepository.save(admin);
    }
}
