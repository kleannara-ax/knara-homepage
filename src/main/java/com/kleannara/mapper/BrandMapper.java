package com.kleannara.mapper;

import com.kleannara.model.BrandModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BrandMapper {
    List<BrandModel> getBrandList(final BrandModel params);
    int getBrandListCount(final SearchModel params);
    BrandModel getBrandOne(final BrandModel params);
    int insertBrand(final BrandModel params);
    int updateBrand(final BrandModel params);
    int deleteBrand(final BrandModel params);
}