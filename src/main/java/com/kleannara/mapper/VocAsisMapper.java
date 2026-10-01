package com.kleannara.mapper;

import com.kleannara.model.SearchModel;
import com.kleannara.model.VocAsisModel;
import com.kleannara.model.VocFileModel;
import com.kleannara.model.VocModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface VocAsisMapper {
    List<VocAsisModel> getVocAsisList(final SearchModel params);
    int getVocAsisListCount(final SearchModel params);
    VocAsisModel getVocAsisOne(final VocAsisModel params);
}