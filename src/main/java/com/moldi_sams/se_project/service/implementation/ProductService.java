package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.*;
import com.moldi_sams.se_project.exception.ResourceAlreadyExistsException;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.ImageMapper;
import com.moldi_sams.se_project.mapper.ProductMapper;
import com.moldi_sams.se_project.repository.*;
import com.moldi_sams.se_project.request.admin.ProductRequest;
import com.moldi_sams.se_project.response.ImageResponse;
import com.moldi_sams.se_project.response.ProductResponse;
import com.moldi_sams.se_project.service.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final ImageMapper imageMapper;
    private final ProductSizeRepository productSizeRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductGenderRepository productGenderRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public Page<ProductResponse> findAll(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("No products could be found");
        }

        return products.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> findAllInStock(Pageable pageable) {
        Page<Product> products = productRepository.findAllByStockGreaterThan(0L, pageable);

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
                .orElseThrow(() -> new ResourceNotFoundException("The product by the provided id could not be found"));
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Optional<Product> searchedProductByName = productRepository
                .findByName(productRequest.name());

        if (searchedProductByName.isPresent()) {
            throw new ResourceAlreadyExistsException("A product with this name already exists");
        }

        ProductGender searchedProductGenderByName = productGenderRepository
                .findByNameIgnoreCase(productRequest.genderName())
                .orElseGet(() -> {
                    ProductGender newGender = ProductGender.builder()
                            .name(productRequest.genderName())
                            .build();
                    return productGenderRepository.save(newGender);
                });

        ProductBrand searchedProductBrandByName = productBrandRepository
                .findByNameIgnoreCase(productRequest.brandName())
                .orElseGet(() -> {
                    ProductBrand newBrand = ProductBrand.builder()
                            .name(productRequest.brandName())
                            .build();
                    return productBrandRepository.save(newBrand);
                });

        ProductCategory searchedProductCategoryByName = productCategoryRepository
                .findByNameIgnoreCase(productRequest.categoryName())
                .orElseGet(() -> {
                    ProductCategory newCategory = ProductCategory.builder()
                            .name(productRequest.categoryName())
                            .build();
                    return productCategoryRepository.save(newCategory);
                });

        List<ProductSize> searchedProductSizesByName = productSizeRepository
                .findAllByNameInIgnoreCase(productRequest.sizesNames());

        List<String> missingSizeNames = productRequest.sizesNames().stream()
                .filter(sizeName -> searchedProductSizesByName.stream().noneMatch(size -> size.getName().equalsIgnoreCase(sizeName)))
                .toList();

        for (String missingSizeName : missingSizeNames) {
            ProductSize newSize = ProductSize.builder()
                    .name(missingSizeName)
                    .build();
            ProductSize savedSize = productSizeRepository.save(newSize);
            searchedProductSizesByName.add(savedSize);
        }

        Product newProduct = Product
                .builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .stock(productRequest.stock())
                .sizes(searchedProductSizesByName)
                .brand(searchedProductBrandByName)
                .category(searchedProductCategoryByName)
                .gender(searchedProductGenderByName)
                .images(new ArrayList<>())
                .build();

        for (MultipartFile image : productRequest.images()) {
            ImageResponse newImage = imageService.save(image);
            newProduct.getImages().add(imageMapper.toImage(newImage));
        }

        return productMapper.toProductResponse(productRepository.save(newProduct));
    }

    @Override
    public ProductResponse updateById(Long productId, ProductRequest productRequest) {
        Product searchedProductById = productRepository
                .findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("The product by the provided id could not be found"));

        Optional<Product> searchedProductByName = productRepository
                .findByName(productRequest.name());

        if (searchedProductByName.isPresent() && !searchedProductById.getName().equalsIgnoreCase(productRequest.name())) {
            throw new ResourceAlreadyExistsException("A product with this name already exists");
        }

        ProductGender searchedProductGenderByName = productGenderRepository
                .findByNameIgnoreCase(productRequest.genderName())
                .orElseGet(() -> {
                    ProductGender newGender = ProductGender.builder()
                            .name(productRequest.genderName())
                            .build();
                    return productGenderRepository.save(newGender);
                });

        ProductBrand searchedProductBrandByName = productBrandRepository
                .findByNameIgnoreCase(productRequest.brandName())
                .orElseGet(() -> {
                    ProductBrand newBrand = ProductBrand.builder()
                            .name(productRequest.brandName())
                            .build();
                    return productBrandRepository.save(newBrand);
                });

        ProductCategory searchedProductCategoryByName = productCategoryRepository
                .findByNameIgnoreCase(productRequest.categoryName())
                .orElseGet(() -> {
                    ProductCategory newCategory = ProductCategory.builder()
                            .name(productRequest.categoryName())
                            .build();
                    return productCategoryRepository.save(newCategory);
                });

        List<ProductSize> searchedProductSizesByName = productSizeRepository
                .findAllByNameInIgnoreCase(productRequest.sizesNames());

        List<String> missingSizeNames = productRequest.sizesNames().stream()
                .filter(sizeName -> searchedProductSizesByName.stream().noneMatch(size -> size.getName().equalsIgnoreCase(sizeName)))
                .toList();

        for (String missingSizeName : missingSizeNames) {
            ProductSize newSize = ProductSize.builder()
                    .name(missingSizeName)
                    .build();
            ProductSize savedSize = productSizeRepository.save(newSize);
            searchedProductSizesByName.add(savedSize);
        }

        searchedProductById.getImages().forEach(imageService::delete);

        searchedProductById.setImages(new ArrayList<>());

        for (MultipartFile image : productRequest.images()) {
            ImageResponse newImage = imageService.save(image);
            searchedProductById.getImages().add(imageMapper.toImage(newImage));
        }

        searchedProductById.setName(productRequest.name());
        searchedProductById.setDescription(productRequest.description());
        searchedProductById.setPrice(productRequest.price());
        searchedProductById.setStock(productRequest.stock());
        searchedProductById.setSizes(searchedProductSizesByName);
        searchedProductById.setBrand(searchedProductBrandByName);
        searchedProductById.setCategory(searchedProductCategoryByName);
        searchedProductById.setGender(searchedProductGenderByName);

        return productMapper.toProductResponse(productRepository.save(searchedProductById));
    }

    @Override
    public void deleteById(Long productId) {
        Optional<Product> searchedProductById = productRepository.findById(productId);

        if (searchedProductById.isEmpty()) {
            throw new ResourceNotFoundException("The product by the given id could not be found");
        }

        List<Order> orders = orderRepository.findAllContainingProductId(productId);

        orderRepository.deleteAll(orders);

        Page<Review> reviews = reviewRepository.findAllByProductProductId(productId, null);

        reviewRepository.deleteAll(reviews);

        searchedProductById.get().getImages().forEach(imageService::delete);

        productRepository.deleteById(productId);
    }
}
