package com.kleannara.mapper;

import com.kleannara.model.ReportModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ReportMapper {
    List<ReportModel> getReportList(final SearchModel params);
    int getReportListCount(final SearchModel params);
    ReportModel getReportOne(final ReportModel params);
    List<ReportModel> getReportNext(final ReportModel params);
    int insertReport(final ReportModel params);

    int mergeReport(final ReportModel params);
    int updateReport(final ReportModel params);
    int deleteReport(final ReportModel params);
}