package com.test.library.service.impl;

import com.test.library.model.Country;
import com.test.library.repository.CountryRepository;
import com.test.library.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class CountryServiceImpl  implements CountryService {
    private final CountryRepository countryRepository;
    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
