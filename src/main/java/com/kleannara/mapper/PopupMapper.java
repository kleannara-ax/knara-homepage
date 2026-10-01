package com.kleannara.mapper;

import com.kleannara.model.PopupModel;
import com.kleannara.model.SearchModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface PopupMapper {
    List<PopupModel> getPopupList(final SearchModel params);
    int getPopupListCount(final SearchModel params);
    PopupModel getPopupOne(final PopupModel params);
    int insertPopup(final PopupModel params);
    int updatePopup(final PopupModel params);
    int deletePopup(final PopupModel params);
}