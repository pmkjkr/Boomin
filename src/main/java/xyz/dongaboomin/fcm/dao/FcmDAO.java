package xyz.dongaboomin.fcm.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import xyz.dongaboomin.fcm.dto.CircleFcmDTO;
import xyz.dongaboomin.fcm.dto.NormalFcmDTO;


import java.util.List;

public class FcmDAO implements FcmModel {
    private Sql2o sql2o;

    public FcmDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<CircleFcmDTO> showCircleNotice(int limit, int offset, int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT id, admin_id, title, body, data, created_at, DATE_FORMAT(updated_at, '%Y/%m/%d') as updated_at FROM pcircle_notis WHERE admin_id=:admin_id ORDER BY id DESC limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("admin_id",admin_id)
                    .addParameter("limit",limit)
                    .addParameter("offset",offset)
                    .executeAndFetch(CircleFcmDTO.class);
        }
    }

    @Override
    public List<NormalFcmDTO> showNormalNotice(int limit, int offset, int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "SELECT id, admin_id, title, body, data, created_at, DATE_FORMAT(updated_at, '%Y/%m/%d') as updated_at  FROM pnotis WHERE admin_id=:admin_id ORDER BY id DESC limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("admin_id",admin_id)
                    .addParameter("limit",limit)
                    .addParameter("offset",offset)
                    .executeAndFetch(NormalFcmDTO.class);
        }
    }

    @Override
    public long countNNTable(int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select count(*) from pnotis where admin_id= :admin_id";
            return conn.createQuery(sql)
                    .addParameter("admin_id",admin_id)
                    .executeScalar(Long.class);
        }
    }

    @Override
    public long countCNTable(int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select count(*) from pcircle_notis where admin_id= :admin_id";
            return conn.createQuery(sql)
                    .addParameter("admin_id",admin_id)
                    .executeScalar(Long.class);
        }
    }
}
