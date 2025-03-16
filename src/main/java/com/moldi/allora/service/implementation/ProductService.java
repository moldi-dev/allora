package com.moldi.allora.service.implementation;

import com.moldi.allora.entity.*;
import com.moldi.allora.exception.ResourceAlreadyExistsException;
import com.moldi.allora.exception.ResourceNotFoundException;
import com.moldi.allora.mapper.ImageMapper;
import com.moldi.allora.mapper.ProductMapper;
import com.moldi.allora.repository.*;
import com.moldi.allora.request.admin.ProductRequest;
import com.moldi.allora.request.user.ProductFilterRequest;
import com.moldi.allora.response.ImageResponse;
import com.moldi.allora.response.ProductResponse;
import com.moldi.allora.service.IProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public Page<ProductResponse> findAllByFilters(ProductFilterRequest request, Pageable pageable) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (request.name() != null && !request.name().isEmpty()) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + request.name().toLowerCase() + "%"
                        )
                );
            }

            if (request.brandsIds() != null && !request.brandsIds().isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("brand").get("productBrandId").in(request.brandsIds()));
            }

            if (request.categoriesIds() != null && !request.categoriesIds().isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("category").get("productCategoryId").in(request.categoriesIds()));
            }

            if (request.sizesIds() != null && !request.sizesIds().isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.join("sizes").get("productSizeId").in(request.sizesIds()));
            }

            if (request.gendersIds() != null && !request.gendersIds().isEmpty()) {
                predicates = criteriaBuilder.and(predicates, root.get("gender").get("productGenderId").in(request.gendersIds()));
            }

            if (request.maxPrice() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.le(root.get("price"), request.maxPrice()));
            }

            if (request.minPrice() != null) {
                predicates = criteriaBuilder.and(predicates, criteriaBuilder.ge(root.get("price"), request.minPrice()));
            }

            return predicates;
        };

        if (request.sort() != null) {
            Sort sort = Sort.by(Sort.Direction.ASC, "name");

            sort = switch (request.sort()) {
                case "name-ascending" -> Sort.by(Sort.Direction.ASC, "name");
                case "name-descending" -> Sort.by(Sort.Direction.DESC, "name");
                case "price-ascending" -> Sort.by(Sort.Direction.ASC, "price");
                case "price-descending" -> Sort.by(Sort.Direction.DESC, "price");

                default -> sort;
            };

            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        Page<Product> products = productRepository.findAll(specification, pageable);

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
