package xyz.dongaboomin.attend.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.attend.dao.AttendDAO;
import xyz.dongaboomin.attend.dao.AttendModel;
import xyz.dongaboomin.attend.dto.AttendDTO;
import xyz.dongaboomin.attend.dto.AttendLetterDTO;

import java.util.List;

/**
 * Created by horse on 2017. 8. 16..
 */
public class AttendController {
    private AttendModel model;

    public AttendController(Sql2o sql2o){
        model = new AttendDAO(sql2o);
    }

    public List<AttendLetterDTO> showAttendLetterList(int limit, int offset, int admin_id){
        return model.showAttendLetterList(limit, offset, admin_id);
    }
    public List<AttendDTO> showAttendList(int limit, int offset, int pcnotis_id){
        return model.showAttendList(limit, offset, pcnotis_id);
    }
    public Long countLetterTable(int admin_id){
        return model.countLetterTable(admin_id);
    }
    public Long countAttendPerson(int pcircle_notis_id){
        return model.countAttendPerson(pcircle_notis_id);
    }
}
