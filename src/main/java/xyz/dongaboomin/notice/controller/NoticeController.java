package xyz.dongaboomin.notice.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.notice.dao.NoticeDAO;
import xyz.dongaboomin.notice.dto.NoticeDTO;
import xyz.dongaboomin.notice.dao.NoticeModel;

import java.util.List;


public class NoticeController {
    private NoticeModel model;

    public NoticeController(Sql2o sql2o){
        model = new NoticeDAO(sql2o);
    }

    public List<NoticeDTO> showNoticeList(){
        return model.showNoticeList();
    }

    public long writeNotice(String title, String contents){
        return model.writeNotice(title,contents);
    }
}
