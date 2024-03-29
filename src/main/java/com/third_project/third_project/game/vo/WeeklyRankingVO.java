package com.third_project.third_project.game.vo;


import com.third_project.third_project.entity.GameScoreEntity;
import com.third_project.third_project.entity.MemberImgEntity;
import com.third_project.third_project.entity.MemberInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.Date;


public interface WeeklyRankingVO {

    Integer getRanking();
    GameScoreEntity game();
}
