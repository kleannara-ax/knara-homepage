package com.kleannara.mapper;

import com.kleannara.model.BrandCategoryModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BrandCategoryMapper {
    List<BrandCategoryModel> getBrandCategoryList(final BrandCategoryModel params);
    int getBrandCategoryListCount(final SearchModel params);
    BrandCategoryModel getBrandCategoryOne(final BrandCategoryModel params);
    int insertBrandCategory(final BrandCategoryModel params);
    int updateBrandCategory(final BrandCategoryModel params);
    int deleteBrandCategory(final BrandCategoryModel params);
}