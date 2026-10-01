package com.kleannara.service;


import com.kleannara.mapper.ReportMapper;
import com.kleannara.model.ReportModel;
import com.kleannara.model.SearchModel;
import com.kleannara.paging.paging.Pagination;
import com.kleannara.paging.paging.PagingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminReportService {

    @Autowired
    public ReportMapper reportMapper;

    public PagingResponse<ReportModel> getReportList(final SearchModel params){
        int count = reportMapper.getReportListCount(params);
        Pagination pagination = new Pagination(count, params);
        params.setPagination(pagination);

        List<ReportModel> list = reportMapper.getReportList(params);
        return new PagingResponse<>(list, pagination);
    }

    public ReportModel getReportOne(final ReportModel params){
        return reportMapper.getReportOne(params);
    }

    public List<ReportModel> getReportNext(final ReportModel params){
        return reportMapper.getReportNext(params);
    }

    public int insertReport(final ReportModel params){
        return reportMapper.insertReport(params);
    }

    public int updateReport(final ReportModel params){
        return reportMapper.updateReport(params);
    }
}
