package com.kleannara.model;

import com.kleannara.paging.paging.Pagination;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileModel {
    private String fileOrig;
    private String fileName;
    private String filePath;
}
