package com.kleannara.service;

import com.kleannara.model.FileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileService {

    @Value("#{environment['config.file.dir']}")
    private String fileDir;

    public FileModel saveFile(MultipartFile files, String path) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // MultipartFile로 부터 file을 얻음
        String extensionLow = extension.toLowerCase();

        // 화이트리스트 방식으로 업로드 파일의 학장자를 체크한다.
        if (extensionLow != null) {
            if (extensionLow.endsWith(".doc") || extensionLow.endsWith(".hwp") || extensionLow.endsWith(".pdf") || extensionLow.endsWith(".xls")
                    || extensionLow.endsWith(".png") || extensionLow.endsWith(".jpg") || extensionLow.endsWith(".gif") || extensionLow.endsWith(".jpeg")
                    || extensionLow.endsWith(".mp4")|| extensionLow.endsWith(".avi")) {
                /* file 업로드 루틴 */
            } else {
                throw new IOException("에러");
            }
        }

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = fileDir + '/' + path + '/' + savedName;
        File destDir = new File(fileDir + '/' + path + '/');

        // 파일 엔티티 생성
        FileModel file = FileModel.builder()
                .fileOrig(origName)
                .fileName(savedName)
                .filePath(savedPath)
                .build();

        if(!destDir.exists()){
            destDir.mkdirs(); //디렉토리가 존재하지 않는다면 생성
        }

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new File(savedPath));

        return file;
    }
}