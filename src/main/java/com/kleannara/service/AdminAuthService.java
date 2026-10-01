package com.kleannara.service;


import com.kleannara.mapper.AdminLogMapper;
import com.kleannara.mapper.AdminMapper;
import com.kleannara.model.SearchModel;
import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdminAuthService {

    @Autowired
    public AdminMapper adminMapper;
    
    @Autowired
    public AdminLogMapper adminLogMapper;

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

    public int insertAdmin(final AdminModel params){
        return adminMapper.insertAdmin(params);
    }

    public int updateAdmin(final AdminModel params){
        return adminMapper.updateAdmin(params);
    }
    
    public int insertAdminLog(final AdminLogModel params){
    	return adminLogMapper.insertAdminLog(params);
    }

    public int getAdminStatus(String id){ return adminMapper.getAdminStatus(id); }
    public String getAdminPasswordCheck(final AdminModel params){
        AdminModel result = adminMapper.getAdminPasswordCheck(params);
        return result.getPasswordChangeFlg();
    }

    public List<AdminModel> getLoginDisableList(){ return adminMapper.getLoginDisableList(); }

    public int updateLoginStatus(List<AdminModel> list){ return adminMapper.updateLoginStatus(list); }
}
