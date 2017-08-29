package xyz.dongaboomin.manage.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.manage.dao.ManageDAO;
import xyz.dongaboomin.manage.dao.ManageModel;
import xyz.dongaboomin.manage.dto.ManageDTO;
import xyz.dongaboomin.manage.dto.PartManageDTO;

import java.util.List;

public class ManageController {
    private ManageModel model;

    public ManageController(Sql2o sql2o){
        model = new ManageDAO(sql2o);
    }
    public List<PartManageDTO> showUserManageList(int limit, int offset, int circle_id){
        return model.showUserManageList(limit, offset, circle_id);
    }
    public Long countTable(int circle_id){
        return model.countTabel(circle_id);
    }

}
