package com.third_project.third_project.game.service;

import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.entity.GameScoreEntity;
import com.third_project.third_project.entity.MemberInfoEntity;
import com.third_project.third_project.game.vo.ScorePercentResponseVO;
import com.third_project.third_project.entity.MemberScoreView;
import com.third_project.third_project.game.vo.ScoreResponseVO;
import com.third_project.third_project.repository.ExTypeRepository;
import com.third_project.third_project.repository.GameScoreRepository;
import com.third_project.third_project.repository.MemberInfoRepository;
import com.third_project.third_project.repository.MemberScoreViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;



import java.time.LocalDate;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ScoreService {
    private final GameScoreRepository gsRepo;
    private final MemberInfoRepository miRepo;
    private final MemberScoreViewRepository msRepo;


    public ScorePercentResponseVO getMemberPercent(LocalDate day, Long seq){
        MemberInfoEntity member = miRepo.findByMiSeq(seq);
        GameScoreEntity entity = gsRepo.findByMember(member);
        List<GameScoreEntity> list = gsRepo.findByGsRegDt(day);

        if(entity == null) {
            ScorePercentResponseVO response = ScorePercentResponseVO.builder()
                    .status(false)
                    .message("조회된 회원 정보가 없습니다")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();
            return response;
        }
        Integer people = 0;
        for(int i=0; i<list.size(); i++){
            people += 1;
        }


        ScorePercentResponseVO response = ScorePercentResponseVO.builder()
                .status(true)
                .message("조회된 회원 성적의 상위 퍼센트 조회!!")
                .code(HttpStatus.OK)
                .build();
        return response;
    }



    public ScoreResponseVO getMemberScore(Long seq) { //회원 게임점수 조회
        MemberInfoEntity member = miRepo.findByMiSeq(seq); //입력받은 번호에 해당하는 회원 조회
        GameScoreEntity mentity = gsRepo.findByMember(member);
        MemberScoreView msentity = msRepo.findByMiSeq(seq);


        if(mentity == null) {
            ScoreResponseVO response = ScoreResponseVO.builder()
                    .status(false)
                    .message("조회된 회원 정보가 없습니다")
                    .code(HttpStatus.BAD_REQUEST)
                    .build();

            return response;
        }

        if(msentity.getEtTimeType()==1){
          

        }

        return null;
    }

}
