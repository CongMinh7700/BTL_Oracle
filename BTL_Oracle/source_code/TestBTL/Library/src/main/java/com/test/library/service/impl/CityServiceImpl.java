package com.test.library.service.impl;

import com.test.library.model.City;
import com.test.library.repository.CityRepository;
import com.test.library.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }
}
