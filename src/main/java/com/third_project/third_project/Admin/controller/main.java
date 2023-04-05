package com.third_project.third_project.Admin.controller;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.third_project.third_project.Admin.service.NoticeAdminService;
import com.third_project.third_project.Admin.vo.NotiiceAdminInsertVO;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class main {
  private final NoticeAdminService noticeAdminService;
  @GetMapping("/")
  public String getMain(Model model, @RequestParam@Nullable String keyword,
  @PageableDefault(size = 10, sort="gnSeq",direction = Sort.Direction.DESC)Pageable pageable
  ,HttpSession session){
    if(keyword == null) keyword = "";
    model.addAttribute("result", noticeAdminService.getNoticeList(keyword, pageable));
    model.addAttribute("keyword", keyword);
    return "/main";
  }
  @GetMapping("/add")
  public String getNoticeAdd(){
    return "/add";
  }
  @PostMapping("/add")
  public String postNotice(Model model, NotiiceAdminInsertVO data){
    Map<String,Object> map = noticeAdminService.addNotice(data);
    model.addAttribute("data", data);
    model.addAttribute("result", map);
        return "redirect:/";
    }
  @GetMapping("/delete")
  public String deleteNotice (@RequestParam Long notice_no){
    noticeAdminService.deleteNotice(notice_no);
    return "redirect:/";
  }
  @GetMapping("/notice/video")
  public String getVideo(){
    return "/notice/video";
  }
}
