package com.kleannara.service;


import com.kleannara.mapper.NewsMapper;
import com.kleannara.model.NewsModel;
import com.kleannara.model.ReportModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminNewsService {

    @Autowired
    public NewsMapper newsMapper;

    // 브랜드
    public PagingResponse<NewsModel> getNewsList(final SearchModel params){
        int count = newsMapper.getNewsListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<NewsModel> list = newsMapper.getNewsList(params);
        return new PagingResponse<>(list, pagination);
    }

    public NewsModel getNewsOne(final NewsModel params){
        return newsMapper.getNewsOne(params);
    }

    public NewsModel getNewsTopOne(){
        return newsMapper.getNewsTopOne();
    }

    public List<NewsModel> getNewsNext(final NewsModel params){
        return newsMapper.getNewsNext(params);
    }

    public int insertNews(final NewsModel params){
        return newsMapper.insertNews(params);
    }

    public int updateNews(final NewsModel params){
        return newsMapper.updateNews(params);
    }

    public int updateNewsOrder(final NewsModel params){
        return newsMapper.updateNewsOrder(params);
    }
}
