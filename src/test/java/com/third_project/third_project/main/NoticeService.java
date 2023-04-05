package com.third_project.third_project.main;



import java.io.File;
import java.nio.file.Paths;
import java.util.Calendar;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.jayway.jsonpath.internal.Path;
import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.entity.ExVideoEntity;
import com.third_project.third_project.entity.GameNoticeEntity;
import com.third_project.third_project.main.vo.ErrorResponse;
import com.third_project.third_project.main.vo.exceptionHendler.ExceptionHendler;
import com.third_project.third_project.main.vo.request.POSTGameNoticeVO;
import com.third_project.third_project.main.vo.response.ResponseMessage;
import com.third_project.third_project.repository.ExTypeRepository;
import com.third_project.third_project.repository.ExVideoRepository;
import com.third_project.third_project.repository.GameNoticeRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class NoticeService {

   

        @Autowired GameNoticeRepository gameNoticeRepo;
        @Autowired ExVideoRepository exVideoRepo;
        @Autowired ExTypeRepository exTypeRepo;
        // 영상파일 저장경로
        @Value("${file.video.exercise.notice}") String excriseVideoPath;
       
        // 게임 공지사항 등록( 이미지, 영상 첨부가능 )
        // 입력할때 제목, 내용, 영상, 운동종류를 받음
        @Test
        @Transactional
        public ResponseMessage postNotice(POSTGameNoticeVO data){
            GameNoticeEntity gameNoticeEntity = null;
            ExVideoEntity exVideoEntity = null;
            ExTypeEntity exTypeEntity = null;
            exTypeEntity = exTypeRepo.findByEtSeq(data.getGnEtSeq());
    
            if(data.getGnTitle() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("제목을 입력하세요.")));
            else if(data.getGnContent() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("내용을 입력하세요.")));
            else if(data.getGnEtSeq() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("운동을 선택해주세요.")));
            else if(data.getFiles() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("첨부할 영상을 선택해주세요.")));
            else if(data.getGnEtSeq() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("운동을 선택해주세요.")));
            else if(data.getGnEtSeq() == null)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("선택한 운동이 존재하지 않습니다.")));            
            else if(exTypeEntity.getEtEsSeq() != 3)
                throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("게임용 운동을 선택해주세요.")));
    
            else{
                try{
                    String contentType = data.getFiles().getContentType();
                    String originalFileExtension = "";
                    if (contentType.contains("video/mp4")) {
                        originalFileExtension = ".mp4";
                    }
                        // String url = UUID.randomUUID().toString();
    
                        // if (ObjectUtils.isEmpty(contentType))  // 확장자명이 없다면(잘못된 파일)
                        //     break;
                        String newFileName = "Notice_" + Calendar.getInstance().getTimeInMillis() + originalFileExtension;
                        String url = "notice_"+ Calendar.getInstance().getTimeInMillis() + originalFileExtension;
                         exVideoEntity = ExVideoEntity.builder()
                                .evName(newFileName)
                                .evUrl(url)
                                .evEtSeq(data.getGnEtSeq())
                                .build();
                        exVideoRepo.save(exVideoEntity);
                        File file = new File(excriseVideoPath);
                        // 저장할 위치의 디렉토리가 존재하지 않을 경우 디렉토리를 생성
                        if(!file.exists())  file.mkdirs();
                        java.nio.file.Path savePath = Paths.get(excriseVideoPath+File.separator+newFileName);
                        try{
                        data.getFiles().transferTo(file);
                    }
                        catch(Exception e){
                            e.printStackTrace();
                        }
                }
                catch(Exception e){ 
                    e.printStackTrace();
                }
                // ExVideoEntity exVideoEntity = exVideoRepo.findByEvSeq(());
                gameNoticeEntity = GameNoticeEntity.builder()
                .gnTitle(data.getGnTitle())
                .gnContent(data.getGnContent())
                .gnEtSeq(data.getGnEtSeq())
                .gnMiSeq(data.getGnMiSeq())
                .gnEvSeq(exVideoEntity.getEvSeq())
                .gnRegDt(null)
                .build();
    
                gameNoticeRepo.save(gameNoticeEntity);
                ResponseMessage response = new ResponseMessage(true, HttpStatus.OK, "공지사항이 등록되었습니다.");
                return response;
            }
        }
}
