package com.third_project.third_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="individual_score")
public class IndividualScoreEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="is_seq")     private Long isSeq;
    @Column(name="is_mi_seq")  private Long isMiSeq;
    @Column(name="is_et_seq")  private Long isEtSeq;
    @Column(name="is_reg_dt")  private LocalDate isRegDt;
    @Column(name="is_time")    private LocalTime isTime;
}

