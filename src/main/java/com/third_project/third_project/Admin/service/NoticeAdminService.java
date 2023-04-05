package com.third_project.third_project.Admin.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.third_project.third_project.Admin.entity.NoticeAdminEntity;
import com.third_project.third_project.Admin.repository.NoticeAdminRepository;
import com.third_project.third_project.Admin.vo.NoticeAdminResponseVO;
import com.third_project.third_project.Admin.vo.NoticeResponseVO;
import com.third_project.third_project.Admin.vo.NotiiceAdminInsertVO;
import com.third_project.third_project.entity.GameNoticeEntity;
import com.third_project.third_project.repository.GameNoticeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticeAdminService {
  private final NoticeAdminRepository noticeAdminRepository;

  public NoticeAdminResponseVO getNoticeList(String keyword, Pageable pageable){
    Page<NoticeAdminEntity> page = noticeAdminRepository.findByGnTitleContains(keyword, pageable);
    NoticeAdminResponseVO response = NoticeAdminResponseVO.builder()
    .list(page.getContent())
    .total(page.getTotalElements())
    .totalPage(page.getTotalPages())
    .currentPage(page.getNumber())
    .build();
    return response;
  }

  public Map<String,Object> addNotice(NotiiceAdminInsertVO data){
    Map<String,Object> map = new LinkedHashMap<String,Object>();
    NoticeAdminEntity entity = NoticeAdminEntity.builder()
    .gnTitle(data.getGnTitle())
    .gnContent(data.getGnContent())
    .gnEtSeq(data.getGnEtSeq())
    .gnEvSeq(data.getGnEvSeq())
    .gnMiSeq(data.getGnMiSeq())
    .build();
    noticeAdminRepository.save(entity);
    return map;
  }
  @Transactional
  public void deleteNotice(Long gnSeq){
    noticeAdminRepository.deleteById(gnSeq);
  }
}
