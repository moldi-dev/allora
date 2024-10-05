package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.Product;
import com.moldi_sams.se_project.exception.ResourceAlreadyExistsException;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.ProductMapper;
import com.moldi_sams.se_project.repository.ProductRepository;
import com.moldi_sams.se_project.request.ProductRequest;
import com.moldi_sams.se_project.response.ProductResponse;
import com.moldi_sams.se_project.service.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products could be found");
        }

        return products.map(productMapper::toProductResponse);
    }

    @Override
    public ProductResponse findById(Long productId) {
        return productRepository
                .findById(productId)
                .map(productMapper::toProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException("The product by the given id could not be found"));
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Optional<Product> searchedProductByName = productRepository.findByProductName(productRequest.productName());

        if (searchedProductByName.isPresent()) {
            throw new ResourceAlreadyExistsException("A product with the given name already exists");
        }

        Product newProduct = Product
                .builder()
                .productName(productRequest.productName())
                .productDescription(productRequest.productDescription())
                .productPrice(productRequest.productPrice())
                .build();

        return productMapper.toProductResponse(productRepository.save(newProduct));
    }

    @Override
    public ProductResponse updateById(Long productId, ProductRequest productRequest) {
        Product searchedProductById = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("The product by the given id could not be found"));

        searchedProductById.setProductName(productRequest.productName());
        searchedProductById.setProductDescription(productRequest.productDescription());
        searchedProductById.setProductPrice(productRequest.productPrice());

        return productMapper.toProductResponse(productRepository.save(searchedProductById));
    }

    @Override
    public void deleteById(Long productId) {
        Optional<Product> searchedProductById = productRepository.findById(productId);

        if (searchedProductById.isEmpty()) {
            throw new ResourceNotFoundException("The product by the given id could not be found");
        }

        productRepository.deleteById(productId);
    }
}
