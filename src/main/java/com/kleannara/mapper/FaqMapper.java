package com.kleannara.mapper;

import com.kleannara.model.FaqModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface FaqMapper {
    List<FaqModel> getFaqList(final SearchModel params);
    int getFaqListCount(final SearchModel params);
    FaqModel getFaqOne(final FaqModel params);
    int insertFaq(final FaqModel params);
    int updateFaq(final FaqModel params);
    int deleteFaq(final FaqModel params);
}