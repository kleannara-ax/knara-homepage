package com.kleannara.mapper;

import com.kleannara.model.NewsModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface NewsMapper {
    List<NewsModel> getNewsList(final SearchModel params);
    int getNewsListCount(final SearchModel params);
    NewsModel getNewsOne(final NewsModel params);
    NewsModel getNewsTopOne();
    List<NewsModel> getNewsNext(final NewsModel params);
    int insertNews(final NewsModel params);
    int updateNews(final NewsModel params);
    int updateNewsOrder(final NewsModel params);
    int deleteNews(final NewsModel params);
}