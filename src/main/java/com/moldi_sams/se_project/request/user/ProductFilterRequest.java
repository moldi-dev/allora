package com.moldi_sams.se_project.request.user;

import com.moldi_sams.se_project.validation.PriceRange;
import com.moldi_sams.se_project.validation.StringValues;

import java.math.BigDecimal;
import java.util.List;

@PriceRange
public record ProductFilterRequest(
        String name,

        List<Integer> brandsIds,

        List<Integer> categoriesIds,

        List<Integer> sizesIds,

        List<Integer> gendersIds,

        BigDecimal maxPrice,

        BigDecimal minPrice,

        @StringValues(allowedValues = {"name-ascending", "name-descending", "price-ascending", "price-descending"}, message = "The sorting can only contain the values 'name-ascending', 'name-descending', 'price-ascending', 'price-descending'")
        String sort
) {
}
