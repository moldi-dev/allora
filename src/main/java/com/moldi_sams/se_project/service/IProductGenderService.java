package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.response.ProductGenderResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProductGenderService {
    List<ProductGenderResponse> findAll();
    ProductGenderResponse findById(Long productGenderId);
    ProductGenderResponse findByNameIgnoreCase(String name);
}
