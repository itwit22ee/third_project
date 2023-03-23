package com.third_project.third_project.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.third_project.third_project.entity.ExTypeEntity;
import com.third_project.third_project.main.service.LevelExService;
import com.third_project.third_project.main.vo.response.GetLevelExVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "레벨별 운동" , description ="레벨별 운동 리스트조회")
@RestController
@RequestMapping("api/level/exercise")
public class LevelExAPIController {

    @Autowired LevelExService levelExService;

    @Operation(summary = "레벨별 운동 리스트 조회", description = "회원seq(miSeq)를 파라미터로 받아서 회원 타입(성별, 다이어트/웨이트)에 맞는 운동목록 조회")
    @GetMapping("/{miSeq}")
    public ResponseEntity<List<GetLevelExVO>> levelExList(@Parameter(description = "로그인한 회원 번호", example = "2") @PathVariable Long miSeq){
        List<GetLevelExVO> response = levelExService.levelExList(miSeq);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    
}