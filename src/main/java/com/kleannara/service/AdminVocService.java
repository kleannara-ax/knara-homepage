package com.kleannara.service;


import com.kleannara.mapper.VocAsisMapper;
import com.kleannara.mapper.VocMapper;
import com.kleannara.model.*;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminVocService {

    @Autowired
    public VocMapper vocMapper;

    @Autowired
    public VocAsisMapper vocAsisMapper;

    // 브랜드
    public PagingResponse<VocModel> getVocList(final SearchModel params){
        int count = vocMapper.getVocListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<VocModel> list = vocMapper.getVocList(params);
        return new PagingResponse<>(list, pagination);
    }

    public PagingResponse<VocAsisModel> getVocAsisList(final SearchModel params){
        int count = vocAsisMapper.getVocAsisListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<VocAsisModel> list = vocAsisMapper.getVocAsisList(params);
        return new PagingResponse<>(list, pagination);
    }

    public VocModel getVocOne(final VocModel params){
        return vocMapper.getVocOne(params);
    }

    public VocAsisModel getVocAsisOne(final VocAsisModel params){
        return vocAsisMapper.getVocAsisOne(params);
    }

    public int insertVoc(final VocModel params){
    	vocMapper.insertVoc(params);
        return params.getIdx();
    }

    public int updateVoc(final VocModel params){
        return vocMapper.updateVoc(params);
    }

    // 관리자 응답 내용 History 기능 추가 - 2024/06/13 강지선
    public int insertVocAnswers(final VocModel params){
    	vocMapper.insertVocAnswers(params);
    	return params.getIdx(); 
    }
    
    public int insertVocFile(final VocFileModel params){
        return vocMapper.insertVocFile(params);
    }

    public List<VocFileModel> getVocFileList(final VocFileModel params){
        List<VocFileModel> list = vocMapper.getVocFileList(params);
        return list;
    }
    
    public List<VocModel> getVocAnswers(final VocModel params){
        List<VocModel> list = vocMapper.getVocAnswers(params);
        return list;
    }

}
