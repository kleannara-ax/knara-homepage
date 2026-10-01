package com.kleannara.mapper;

import com.kleannara.model.AdminModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AdminMapper {
    List<AdminModel> getAdminList(final SearchModel params);
    int getAdminListCount(final SearchModel params);
    AdminModel getAdminOne(final AdminModel params);
    int updateLoginDate(final AdminModel params);

    int insertAdmin(final AdminModel params);
    int updateAdmin(final AdminModel params);
    int updateAdminPassword(final AdminModel params);
    int deleteAdmin(final AdminModel params);

    int getAdminStatus(String id);
    AdminModel getAdminPasswordCheck(final AdminModel params);

    int updateExpiredPasswords();

    List<AdminModel> getLoginDisableList();

    int updateLoginStatus(List<AdminModel> list);


}