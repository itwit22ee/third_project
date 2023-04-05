package com.third_project.third_project.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.third_project.third_project.entity.ExImgEntity;
import com.third_project.third_project.entity.ExLevelEntity;
import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.entity.MemberInfoEntity;
import com.third_project.third_project.main.vo.ErrorResponse;
import com.third_project.third_project.main.vo.exceptionHendler.ExceptionHendler;
import com.third_project.third_project.main.vo.response.GetLevelExVO;
import com.third_project.third_project.repository.ExImgRepository;
import com.third_project.third_project.repository.ExLevelRepository;
import com.third_project.third_project.repository.ExTypeRepository;
import com.third_project.third_project.repository.MemberInfoRepository;

@SpringBootTest
public class LevelExService {
    
    @Autowired ExTypeRepository exTypeRepo;
    @Autowired MemberInfoRepository memberInfoRepo;
    @Autowired ExImgRepository exImgRepo;
    @Autowired ExLevelRepository exLevelRepo;

    public List<GetLevelExVO> levelExList(Long miSeq, Long levelSeq){
        // miSeq로 회원정보검색
        // 성별(mi_gi_seq 1=남/2=여)과 타입(exStatus 1=다이어트/2=웨이트) 을 가져와서
        // 해당하는 운동 정보 출력 
        MemberInfoEntity memberInfoEntity = memberInfoRepo.findByMiSeq(miSeq);
        List<GetLevelExVO> list = new ArrayList<>();
        ExLevelEntity exLevelEntity = exLevelRepo.findByLevelSeq(levelSeq);
        if(exLevelEntity == null){
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("존재하지 않는 레벨입니다(레벨은 1,2,3레벨까지 있습니다).")));
        }
         if(memberInfoEntity == null){
            throw new ExceptionHendler(ErrorResponse.of(HttpStatus.BAD_REQUEST,String.format("해당하는 회원이 없습니다.")));
        }
        else{
            // 로그인한 회원의 성별과 타입에 맞는 운동종목을 불러옴 
            List<ExTypeEntity> exTypeEntity = exTypeRepo.findByGenAndEtEsSeqAndLevel(memberInfoEntity.getGen(), memberInfoEntity.getExStatus().getEsSeq(),  exLevelEntity);
            // etName = 걷기(15), 줄넘기(12), 달리기(13), 계단오르기(14)
            Long eimgSeq = null;
          
            if (exTypeRepo != null) {
                for(ExTypeEntity exList : exTypeEntity){
                    if(exList.getEtName().contains("달리기"))             eimgSeq = Long.valueOf(13);
                    if(exList.getEtName().contains("줄넘기"))             eimgSeq = Long.valueOf(12);
                    if(exList.getEtName().contains("걷기"))               eimgSeq = Long.valueOf(15);
                    if(exList.getEtName().contains("계단오르기"))          eimgSeq = Long.valueOf(14);

                    if(exList.getEtName().contains("푸쉬업"))             eimgSeq = Long.valueOf(21);
                    if(exList.getEtName().contains("스쿼트"))             eimgSeq = Long.valueOf(22);
                    if(exList.getEtName().contains("윗몸"))               eimgSeq = Long.valueOf(23);
                    if(exList.getEtName().contains("플랭크"))             eimgSeq = Long.valueOf(20);

                    ExImgEntity img = exImgRepo.findByEimgSeq(eimgSeq);
                    if (img != null) {
                        list.add(GetLevelExVO.builder()
                            .etSeq(exList.getEtSeq())
                            .etName(exList.getEtName())
                            .etDetail(exList.getEtDetail())
                            .url(img.getEimgUrl())
                            .build());
                    }
                }
            }
        }
        return list;
    }
}
