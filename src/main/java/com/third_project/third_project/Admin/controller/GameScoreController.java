package com.third_project.third_project.Admin.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.third_project.third_project.Admin.service.AdminGameScoreService;
import com.third_project.third_project.game.service.ScoreService;
import com.third_project.third_project.game.service.StampService;
import com.third_project.third_project.game.vo.BasicResponseVO;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GameScoreController {
  private final AdminGameScoreService adminGameScoreService;
  private final ScoreService scoreService;
  private final StampService stampService;


  @GetMapping("/game")
  public String getGameScore(Model model, @RequestParam @Nullable String keyword,
  @PageableDefault(size = 10, sort="gsTime",direction = Sort.Direction.ASC)Pageable pageable
  ,HttpSession session){
    if(keyword == null) keyword = "";
    model.addAttribute("result", adminGameScoreService.getAdminGameScoreList(keyword, pageable));
    model.addAttribute("keyword", keyword);
    model.addAttribute("currentPage", pageable.getPageNumber());
    return "/game/list";
  }
  @GetMapping("/percent")
  public String getPercent(@RequestParam Long et_seq, Model model,
  @RequestParam @Nullable Integer page, @RequestParam @Nullable String keyword){
    if(keyword == null) keyword = "";
    if(page == null) page = 0;
    Map<String,Object> map = adminGameScoreService.selectPercentInfo(et_seq);
    model.addAttribute("score", map);
    model.addAttribute("page", page);
    model.addAttribute("keyword", keyword);
    return "/game/percent";
  }
  @PostMapping("/update")
  public String postPercent(Model model, Long no){
    BasicResponseVO response = scoreService.setPercent(no);

    model.addAttribute("score", response);
    return "redirect:/game";
  }

  @GetMapping("/stamp")
  public String putStamp(Model model, HttpSession session){
    Map<String,Object> map = new LinkedHashMap<String,Object>() ;
    model.addAttribute("score", map);
    return "/game/stamp";
  }
  
  @PostMapping("/putstamp")
  public String putStamp(Model model, Long no){
    BasicResponseVO reponse = stampService.putStampAvailable(no);
    model.addAttribute("score", reponse);
    return "/game/stamp";
  }

}
