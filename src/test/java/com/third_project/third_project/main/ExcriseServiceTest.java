package com.third_project.third_project.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.third_project.third_project.entity.ExImgEntity;
import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.entity.IndividualScoreEntity;
import com.third_project.third_project.entity.MemberInfoEntity;
import com.third_project.third_project.main.vo.ErrorResponse;
import com.third_project.third_project.main.vo.exceptionHendler.ExceptionHendler;
import com.third_project.third_project.main.vo.response.GetExVO;
import com.third_project.third_project.main.vo.response.GetExerciseTimeVO;
import com.third_project.third_project.main.vo.response.GetPersonalExListVO;
import com.third_project.third_project.main.vo.response.PostExRecord;
import com.third_project.third_project.main.vo.response.ResponseMessage;
import com.third_project.third_project.repository.ExTypeRepository;
import com.third_project.third_project.repository.ExVideoRepository;
import com.third_project.third_project.repository.GameNoticeRepository;
import com.third_project.third_project.repository.IndividualScoreRepository;
import com.third_project.third_project.repository.MemberInfoRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class ExcriseServiceTest {
    
    @Autowired GameNoticeRepository gameNoticeRepo;
    @Autowired ExVideoRepository exVideoRepo;
    @Autowired ExTypeRepository exTypeRepo;
    @Autowired IndividualScoreRepository individualScoreRepo;
    @Autowired MemberInfoRepository memberInfoRepo;


    @Test
    @Transactional
    public List<GetPersonalExListVO> getExList(){
        List<GetExVO> data = exTypeRepo.findByEtEsSeq(Long.valueOf(4));
        List<GetPersonalExListVO> personalExList = new ArrayList<>();
        if (data != null) {
            for(GetExVO exList : data){
                ExImgEntity img = exList.getImg();
                if (img != null) {
                    personalExList.add(GetPersonalExListVO.builder()
                        .etSeq(exList.getEtSeq())
                        .etName(exList.getEtName())
                        .etDetail(exList.getEtDetail())
                        .url(img.getEimgUrl())
                        .build());
                }
            }
        }
        return personalExList;
    }

    @Test
    @Transactional
    public ResponseMessage postExRecord(PostExRecord data){
        ResponseMessage response = null;
        IndividualScoreEntity individualRecord = null;
        ExTypeEntity exType = exTypeRepo.findByEtSeq(data.getEtSeq());

        if (data.getMiSeq() == null) 
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("로그인을 해주세요")));
        else if (data.getEtSeq() == null) 
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("운동 종목을 입력하세요")));
        else if (exType.getEtEsSeq() != 4)
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("잘못된 운동종목입니다")));
        else if (exType.getEtEsSeq() == null)
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("존재하지않는 운동종목입니다")));
        else if (data.getTime() == null) 
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("측정한 시간을 입력하세요")));
        else{
            individualRecord = IndividualScoreEntity.builder()
            .isMiSeq(data.getMiSeq())
            .isEtSeq(data.getEtSeq())
            .isTime(data.getTime())
            .build();
            individualScoreRepo.save(individualRecord);
            response = new ResponseMessage(true, HttpStatus.OK, "등록되었습니다.");
        }
        return response;
    }


    public List<GetExerciseTimeVO> getExRecod(Long miSeq, LocalDate startDate, LocalDate endDate ){
        MemberInfoEntity memberinfoEntity = memberInfoRepo.findByMiSeq(miSeq);
        if(memberinfoEntity == null)
        throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("로그인을 해주세요")));
        List<GetExerciseTimeVO> personalExScore = individualScoreRepo.getExerciseTimeByPeriod(miSeq, startDate, endDate);
        return personalExScore;
    }
    
}
