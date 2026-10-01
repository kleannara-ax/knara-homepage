package com.kleannara.service;


import com.kleannara.mapper.BannerMapper;
import com.kleannara.mapper.PopupMapper;
import com.kleannara.model.BannerModel;
import com.kleannara.model.PopupModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminMainService {

    @Autowired
    public BannerMapper mapper;

    @Autowired
    public PopupMapper popupMapper;

    //배너서비스
    public PagingResponse<BannerModel> getBannerList(final SearchModel params){
        int count = mapper.getBannerListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<BannerModel> list = mapper.getBannerList(params);
        return new PagingResponse<>(list, pagination);
    }

    public BannerModel getBannerOne(final BannerModel params){
        return mapper.getBannerOne(params);
    }

    public int insertBanner(final BannerModel params){
        return mapper.insertBanner(params);
    }

    public int updateBanner(final BannerModel params){
        return mapper.updateBanner(params);
    }

    //팝업 서비스
    public PagingResponse<PopupModel> getPopupList(final SearchModel params){
        int count = popupMapper.getPopupListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<PopupModel> list = popupMapper.getPopupList(params);
        return new PagingResponse<>(list, pagination);
    }

    public PopupModel getPopupOne(final PopupModel params){
        return popupMapper.getPopupOne(params);
    }

    public int insertPopup(final PopupModel params){
        return popupMapper.insertPopup(params);
    }

    public int updatePopup(final PopupModel params){
        return popupMapper.updatePopup(params);
    }
}
