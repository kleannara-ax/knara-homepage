package com.kleannara.service;


import com.kleannara.mapper.FaqMapper;
import com.kleannara.model.FaqModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminFaqService {

    @Autowired
    public FaqMapper faqMapper;

    // 브랜드
    public PagingResponse<FaqModel> getFaqList(final SearchModel params){
        int count = faqMapper.getFaqListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<FaqModel> list = faqMapper.getFaqList(params);
        return new PagingResponse<>(list, pagination);
    }

    public FaqModel getFaqOne(final FaqModel params){
        return faqMapper.getFaqOne(params);
    }

    public int insertFaq(final FaqModel params){
        return faqMapper.insertFaq(params);
    }

    public int updateFaq(final FaqModel params){
        return faqMapper.updateFaq(params);
    }
}
