package com.kleannara.service;


import com.kleannara.mapper.AdminMapper;
import com.kleannara.mapper.AdminMapper;
import com.kleannara.model.AdminModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AdminLoginService {

    @Autowired
    public AdminMapper adminMapper;

    // 브랜드
    public PagingResponse<AdminModel> getAdminList(final SearchModel params){
        int count = adminMapper.getAdminListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<AdminModel> list = adminMapper.getAdminList(params);
        return new PagingResponse<>(list, pagination);
    }

    public AdminModel getAdminOne(final AdminModel params){
        return adminMapper.getAdminOne(params);
    }

    public int updateLoginDate(final AdminModel params){ return adminMapper.updateLoginDate(params); }

    public int insertAdmin(final AdminModel params){
        return adminMapper.insertAdmin(params);
    }

    public int updateAdmin(final AdminModel params){
        return adminMapper.updateAdmin(params);
    }

    public int updateAdminPassword(final AdminModel params){
        return adminMapper.updateAdminPassword(params);
    }

    @Transactional
    public int updateExpiredPasswords(){
        return adminMapper.updateExpiredPasswords();
    }

    public List<AdminModel> getAllUserLoginDate(){ return adminMapper.getLoginDisableList(); }

}
