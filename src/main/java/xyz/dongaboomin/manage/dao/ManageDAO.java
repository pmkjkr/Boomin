package xyz.dongaboomin.manage.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import xyz.dongaboomin.manage.dto.ManageDTO;
import xyz.dongaboomin.manage.dto.PartManageDTO;

import java.util.List;

public class ManageDAO implements ManageModel{

    private Sql2o sql2o;

    public ManageDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<PartManageDTO> showUserManageList(int limit, int offset, int circle_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select a.id as id,stuId,name,major from normal_users a join user_circles b on a.id = b.user_id where circle_id = :circle_id order by a.created_at desc limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("circle_id",circle_id)
                    .addParameter("limit",limit)
                    .addParameter("offset",offset)
                    .executeAndFetch(PartManageDTO.class);
        }
    }

    @Override
    public long countTabel() {
        try (Connection conn = sql2o.open()) {
            String sql = "select count(*) from normal_users";
            return conn.createQuery(sql)
                    .executeScalar(Long.class);
        }
    }
}
