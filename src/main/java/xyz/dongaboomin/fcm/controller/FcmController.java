package xyz.dongaboomin.fcm.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.fcm.dao.FcmDAO;
import xyz.dongaboomin.fcm.dao.FcmModel;
import xyz.dongaboomin.fcm.dto.CircleFcmDTO;
import xyz.dongaboomin.fcm.dto.NormalFcmDTO;

import java.util.List;

public class FcmController {
    private FcmModel model;

    public FcmController(Sql2o sql2o){
        model = new FcmDAO(sql2o);
    }

    public List<NormalFcmDTO> showNormalNotice(int limit, int offset, int admin_id){
        return model.showNormalNotice(limit, offset, admin_id);
    }

    public List<CircleFcmDTO> showCircleNotice(int limit, int offset, int admin_id){
        return model.showCircleNotice(limit, offset, admin_id);
    }

    public Long countNNTable(int admin_id){
        return model.countNNTable(admin_id);
    }

    public Long countCNTable(int admin_id){
        return model.countCNTable(admin_id);
    }

}
