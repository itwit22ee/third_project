package com.third_project.third_project.entity;

import com.third_project.third_project.Detail.vo.IsSumScoreVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@DynamicInsert
@Table(name="individual_score")
public class IndividualScoreEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "개인 운동 seq")
    @Column(name="is_seq")     private Long isSeq;
    @Column(name="is_mi_seq")  private Long isMiSeq;
    @Column(name="is_et_seq")  private Long isEtSeq;
    @Column(name="is_reg_dt")  private @ColumnDefault("current_timestamp") LocalDate isRegDt;
    @Column(name="is_time")    private LocalTime isTime;
    @Column(name = "is_week") private Integer isWeed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="is_mi_seq", insertable=false, updatable=false)  private MemberInfoEntity member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="is_et_seq", insertable=false, updatable=false)  private ExTypeEntity exType;


}


