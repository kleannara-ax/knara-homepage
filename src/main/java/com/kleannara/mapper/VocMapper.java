package com.kleannara.mapper;

import com.kleannara.model.VocAsisModel;
import com.kleannara.model.VocFileModel;
import com.kleannara.model.VocModel;
import com.kleannara.model.SearchModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface VocMapper {
    List<VocModel> getVocList(final SearchModel params);
    List<VocModel> getVocAnswers(final VocModel params);
    int getVocListCount(final SearchModel params);
    VocModel getVocOne(final VocModel params);
    VocAsisModel getVocAsisOne(final VocAsisModel params);
    int insertVoc(final VocModel params);
    int insertVocAnswers(final VocModel params);
    int updateVoc(final VocModel params);
    int deleteVoc(final VocModel params);
    int insertVocFile(final VocFileModel params);
    List<VocFileModel> getVocFileList(final VocFileModel params);
}