package com.kleannara.mapper;

import com.kleannara.model.ProductModel;
import com.kleannara.model.SearchModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface ProductMapper {
    List<ProductModel> getProductList(final ProductModel params);
    int getProductListCount(final ProductModel params);
    ProductModel getProductOne(final ProductModel params);
    int insertProduct(final ProductModel params);
    int updateProduct(final ProductModel params);
    int deleteProduct(final ProductModel params);
}