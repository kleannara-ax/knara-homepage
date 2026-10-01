package com.kleannara.mapper;

import com.kleannara.model.AdminLogModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.AdminModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface AdminLogMapper {
	List<AdminLogModel> getAdminLogList(final SearchModel params);
    int getAdminLogListCount(final SearchModel params);
    AdminLogModel getAdminOne(final AdminLogModel params);

	int insertAdminLog(final AdminLogModel params);
}