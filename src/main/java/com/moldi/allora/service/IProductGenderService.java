package com.moldi.allora.service;

import com.moldi.allora.response.ProductGenderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductGenderService {
    List<ProductGenderResponse> findAll();
    ProductGenderResponse findById(Long productGenderId);
    ProductGenderResponse findByNameIgnoreCase(String name);
}
