package com.third_project.third_project.Detail.service;

import com.third_project.third_project.Detail.vo.*;
import com.third_project.third_project.entity.*;
import com.third_project.third_project.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.weaver.Lint;
import org.springframework.http.HttpStatus;

import com.third_project.third_project.Detail.vo.IndividualScoreInsertVO;
import com.third_project.third_project.Detail.vo.IndividualScoreListVO;
import com.third_project.third_project.Detail.vo.IndividualScoreRankViewResposeVO;
import com.third_project.third_project.Detail.vo.IndividualScoreResponseVO;
import com.third_project.third_project.Detail.vo.ScoreListViewResponseVO;
import com.third_project.third_project.Detail.vo.ScoreRankListViewResponseVO;
import com.third_project.third_project.Detail.vo.WeeklyScoreViewVO;
import com.third_project.third_project.Detail.vo.updateIndividualScoreInsertVO;
import com.third_project.third_project.entity.IndividualScoreEntity;
import com.third_project.third_project.entity.IndividualScoreRankView;
import com.third_project.third_project.entity.MemberInfoEntity;
import com.third_project.third_project.entity.ScoreListView;
import com.third_project.third_project.entity.ScoreRankListView;
import com.third_project.third_project.entity.WeeklyScoreView;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IndividualScoreService {
  private final IndividualScoreRepository isRepo;
  private final MemberInfoRepository miRepo;
  private final ExTypeRepository etRepo;
  private final ScoreListViewRepository slRepo;
  private final WeeklyScoreViewRepository wsRepo;
  private final IndividualScoreRankViewRepository isrvRepo;
  private final SumScoreNameViewRepo sumScoreNameViewRepo;
  private final SumScoreDateViewRepo sumScoreDateViewRepo;

// 개인기록 운동 별 합산
  public SumScoreNameVO getSumScoreName(Long seq){
    List<SumScoreNameViewEntity> list = sumScoreNameViewRepo.findByMiSeq(seq);
    if(list.isEmpty()){
      SumScoreNameVO response = SumScoreNameVO.builder()
              .status(false)
              .message("조회된 정보가 없습니다.")
              .code(HttpStatus.BAD_REQUEST)
              .build();
      return response;
    }
      SumScoreNameVO response = SumScoreNameVO.builder()
              .status(true)
              .message("해당 종목 기록 총합 조회!!")
              .code(HttpStatus.OK)
              .score(list)
              .build();
      return response;

  }

  // 개인 기록 날짜 별 합산
  public SumScoreDateVO getSumScoreDate(Long seq){
    List<SumScoreDateViewEntity> list = sumScoreDateViewRepo.findByMiSeq(seq);
    if(list.isEmpty()){
      SumScoreDateVO response = SumScoreDateVO.builder()
              .status(false)
              .message("조회된 정보가 없습니다.")
              .code(HttpStatus.BAD_REQUEST)
              .build();
      return response;
    }
    SumScoreDateVO response = SumScoreDateVO.builder()
              .status(true)
              .message("해당 일 기록 총합 조회!!")
              .code(HttpStatus.OK)
              .score(list)
              .build();
      return response;

  }

  //개인 기록 추가
  public IndividualScoreResponseVO addIndividualScore(IndividualScoreInsertVO data){
    if(data == null){
      IndividualScoreResponseVO response = IndividualScoreResponseVO.builder()
      .code(HttpStatus.BAD_REQUEST)
      .message("내용을 입력해주세요")
      .status(false)
      .build();
      return response;
    }
    else{
      if (miRepo.findById(data.getIsMiSeq()).isEmpty()) return IndividualScoreResponseVO.builder().code(HttpStatus.BAD_REQUEST).message("miRepo error").status(false).build();
      else if (etRepo.findById(data.getIsEtSeq()).isEmpty()) return IndividualScoreResponseVO.builder().code(HttpStatus.BAD_REQUEST).message("etRepo error").status(false).build();
      else {
        IndividualScoreEntity entity = IndividualScoreEntity.builder()
        // .member(miRepo.findById(data.getIsMiSeq()).get())
        // .exType(etRepo.findById(data.getIsEtSeq()).get())
        .isMiSeq(miRepo.findById(data.getIsMiSeq()).get().getMiSeq())
        .isEtSeq(etRepo.findById(data.getIsEtSeq()).get().getEtSeq())
        .isRegDt(data.getIsRegDt())
        .isTime(data.getIsTime())
        .isWeed(data.getIsWeek())
        .build();
        isRepo.save(entity);
      }
    }
    return IndividualScoreResponseVO.builder().code(HttpStatus.OK).message("개인 기록이 추가되었습니다..").status(true).build();
  }
// 개인 기록 수정
  public IndividualScoreResponseVO updateIndividualScore(updateIndividualScoreInsertVO data,Long siSeq){
    
    IndividualScoreEntity entity = isRepo.findById(siSeq).get();
    if(data.getUpateIsEtSeq() == null){
      data.setUpateIsEtSeq(entity.getExType().getEtSeq());
    }
    if(data.getUpateIsTime() == null){
      data.setUpateIsTime(entity.getIsTime());
    }
    entity.setExType(etRepo.findById(data.getUpateIsEtSeq()).get());
    entity.setIsTime(data.getUpateIsTime());
    isRepo.save(entity);
    return IndividualScoreResponseVO.builder().code(HttpStatus.OK).message("개인 기록이 수정되었습니다..").status(true).build();
  }

  //개인기록 삭제
  @Transactional
  public IndividualScoreResponseVO deleteIndividualScore(Long siSeq){
    isRepo.deleteById(siSeq);
    return IndividualScoreResponseVO.builder().code(HttpStatus.OK).message("개인 기록이 삭제되었습니다..").status(true).build();
  }

 //개인 기록 조회
public ScoreListViewResponseVO getListScore(Long memberNo){
  List<ScoreListView> member = slRepo.findByIsMiSeq(memberNo);
  // ScoreListView member = slRepo.findByIsMiSeq(memberNo);
  if(member.isEmpty()){
    ScoreListViewResponseVO response = ScoreListViewResponseVO.builder()
    .code(HttpStatus.NOT_FOUND)
    .message(memberNo+"학생의 기록이 존재하지 않습니다.")
    .status(true)
    .build();
    return response;
}
ScoreListViewResponseVO response = ScoreListViewResponseVO.builder()
  .list(member)
  .code(HttpStatus.OK)
  .message(memberNo+"학생의 기록이 조회되었습니다.")
  .status(true)
  .build();
  return response;
}
//이번주 기록 조회
public WeeklyScoreViewVO getWeeklyScore(Long memberNo, Integer week){
  List<WeeklyScoreView> member = wsRepo.findByIsMiSeq(memberNo);
  if(member.isEmpty()){
    WeeklyScoreViewVO response = WeeklyScoreViewVO.builder()
    .message(memberNo+"학생은 등록되지 않은 회원입니다.")
    .code(HttpStatus.NOT_FOUND)
    .status(false)
    .build();
    return response;
  }
  List<WeeklyScoreView> weekly = wsRepo.findByIsMiSeqAndIsWeek(memberNo, week);
  WeeklyScoreViewVO response = WeeklyScoreViewVO.builder()
  .list(weekly)
  .message(memberNo+"학생의 주간 기록이 조회되었습니다..")
  .code(HttpStatus.OK)
  .status(true)
  .build();
  return response;
}
//본인 레벨의 종목별 시간을 기준으로 변화량
public ScoreListViewResponseVO getLevelVariance(Long memberNo, String name){
  List<ScoreListView> member = slRepo.findByIsMiSeq(memberNo);
  if(member.isEmpty()){
    ScoreListViewResponseVO response = ScoreListViewResponseVO.builder()
    .message(memberNo+"학생은 등록되지않은 회원입니다.")
    .status(false)
    .code(HttpStatus.BAD_REQUEST)
    .build();
    return response;
  }
  List<ScoreListView> list = slRepo.findByEtNameAndIsMiSeq(name, memberNo);
  ScoreListViewResponseVO response = ScoreListViewResponseVO.builder()
  .list(list)
  .code(HttpStatus.OK)
  .message(memberNo+"학생의 종목별 기준 변화량이 조회되었습니다.")
  .status(true)
  .build();
  return response;
}
//백분위
  public IndividualScoreRankViewResposeVO getScoreRank(Long memberNo){
    List<IndividualScoreRankView> member = isrvRepo.findByIsMiSeq(memberNo);
    if(member.isEmpty()){
      IndividualScoreRankViewResposeVO respose = IndividualScoreRankViewResposeVO.builder()
      .message(memberNo+"학생은 등록되지 않은 회원입니다.")
      .status(false)
      .code(HttpStatus.NOT_FOUND)
      .build();
      return respose;
    }
    IndividualScoreRankViewResposeVO respose = IndividualScoreRankViewResposeVO.builder()
    .list(member)
    .message(memberNo+"학생의 백분위가 조회되었습니다..")
    .status(true)
    .code(HttpStatus.OK)
    .build();
    return respose;
  }
  }


