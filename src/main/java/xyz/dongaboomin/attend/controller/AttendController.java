package xyz.dongaboomin.attend.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.attend.dao.AttendDAO;
import xyz.dongaboomin.attend.dao.AttendModel;
import xyz.dongaboomin.attend.dto.AttendCountDTO;
import xyz.dongaboomin.attend.dto.AttendDTO;
import xyz.dongaboomin.attend.dto.AttendLetterDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<Integer, Integer> countAttendPerson(int pcircle_notis_id){
        List<AttendCountDTO> attendCountDTOS =  model.countAttendPerson(pcircle_notis_id);
        Map<Integer,Integer> countMap = new HashMap<>();
        for (AttendCountDTO dto : attendCountDTOS) {
            countMap.put(dto.getCheck_att(),dto.getCount_chk());
        }

        return countMap;
    }
}
