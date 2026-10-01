package com.kleannara.service;


import com.kleannara.mapper.BrandCategoryMapper;
import com.kleannara.mapper.BrandMapper;
import com.kleannara.mapper.ProductMapper;
import com.kleannara.model.BrandModel;
import com.kleannara.model.BrandCategoryModel;
import com.kleannara.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBrandService {

    @Autowired
    public BrandMapper brandMapper;

    @Autowired
    public BrandCategoryMapper brandCategoryMapper;

    @Autowired
    public ProductMapper productMapper;

    // 브랜드
    public List<BrandModel> getBrandList(final BrandModel params){
        List<BrandModel> list = brandMapper.getBrandList(params);
        return list;
    }

    public BrandModel getBrandOne(final BrandModel params){
        return brandMapper.getBrandOne(params);
    }

    public int insertBrand(final BrandModel params){
        return brandMapper.insertBrand(params);
    }

    public int updateBrand(final BrandModel params){
        return brandMapper.updateBrand(params);
    }

    // 브랜드 카테고리
    public List<BrandCategoryModel> getBrandCategoryList(final BrandCategoryModel params){
        List<BrandCategoryModel> list = brandCategoryMapper.getBrandCategoryList(params);
        return list;
    }

    public BrandCategoryModel getBrandCategoryOne(final BrandCategoryModel params){
        return brandCategoryMapper.getBrandCategoryOne(params);
    }

    public int insertBrandCategory(final BrandCategoryModel params){
        return brandCategoryMapper.insertBrandCategory(params);
    }

    public int updateBrandCategory(final BrandCategoryModel params){
        return brandCategoryMapper.updateBrandCategory(params);
    }

    //상품
    public List<ProductModel> getProductList(final ProductModel params){
        List<ProductModel> list = productMapper.getProductList(params);
        return list;
    }

    public ProductModel getProductOne(final ProductModel params){
        return productMapper.getProductOne(params);
    }

    public int insertProduct(final ProductModel params){
        return productMapper.insertProduct(params);
    }

    public int updateProduct(final ProductModel params){
        return productMapper.updateProduct(params);
    }
}
