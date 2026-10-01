package com.kleannara.paging.paging;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import com.kleannara.model.VocModel;

@Getter
public class PagingResponse<T> {

    private List<T> list = new ArrayList<>();
    private List<VocModel>  vocAnswers =new ArrayList<>();; 
    private Pagination pagination;

    public PagingResponse(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }

	public void add(List<VocModel> vocAnswers) {
        this.vocAnswers = vocAnswers;
	}

}
