package com.kleannara.mapper;

import com.kleannara.model.BannerModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BannerMapper {
    List<BannerModel> getBannerList(final SearchModel params);
    int getBannerListCount(final SearchModel params);
    BannerModel getBannerOne(final BannerModel params);
    int insertBanner(final BannerModel params);
    int updateBanner(final BannerModel params);
    int deleteBanner(final BannerModel params);
}