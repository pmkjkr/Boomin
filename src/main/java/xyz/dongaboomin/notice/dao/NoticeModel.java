package xyz.dongaboomin.notice.dao;

import xyz.dongaboomin.notice.dto.NoticeDTO;

import java.util.List;

public interface NoticeModel {
    List<NoticeDTO> showNoticeList();
    long writeNotice(String title,String contents);
}
