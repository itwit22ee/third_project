package com.third_project.third_project.repository;


import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.entity.IndividualScoreEntity;
import com.third_project.third_project.main.vo.response.GetExRecodVO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IndividualScoreRepository extends JpaRepository<IndividualScoreEntity, Long> {
  public IndividualScoreEntity findByIsSeq (Long isSeq);
  @Query(value = "select * from individual_score where is_et_seq = :isEtSeq", nativeQuery = true) 
  public IndividualScoreEntity findByIsEtSeq (@Param("isEtSeq") Long isEtSeq);

  
  public List<GetExRecodVO> findByIsMiSeq(Long miSeq);
}
