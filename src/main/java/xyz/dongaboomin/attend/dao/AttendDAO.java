package xyz.dongaboomin.attend.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import xyz.dongaboomin.attend.dto.AttendCountDTO;
import xyz.dongaboomin.attend.dto.AttendDTO;
import xyz.dongaboomin.attend.dto.AttendLetterDTO;
import xyz.dongaboomin.manage.dto.PartManageDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by horse on 2017. 8. 16..
 */
public class AttendDAO implements AttendModel {

    private Sql2o sql2o;

    public AttendDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<AttendLetterDTO> showAttendLetterList(int limit, int offset, int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select id, admin_id, title, body, data, created_at, DATE_FORMAT(updated_at, '%Y/%m/%d') as updated_at from pcircle_notis where admin_id = :admin_id order by id desc limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("admin_id", admin_id)
                    .addParameter("limit", limit)
                    .addParameter("offset", offset)
                    .executeAndFetch(AttendLetterDTO.class);
        }
    }
    @Override
    public List<AttendDTO> showAttendList(int limit, int offset, int pcnotis_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select b.id as id, stuId,name,major,check_att from normal_users a join circle_notis b on a.id = b.user_id where pcircle_notis_id = :pcnotis_id limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("pcnotis_id",pcnotis_id)
                    .addParameter("limit",limit)
                    .addParameter("offset",offset)
                    .executeAndFetch(AttendDTO.class);
        }
    }

    @Override
    public long countLetterTable(int admin_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select count(*) from pcircle_notis where admin_id= :admin_id";
            return conn.createQuery(sql)
                    .addParameter("admin_id",admin_id)
                    .executeScalar(Long.class);
        }
    }

    @Override
    public List<AttendCountDTO> countAttendPerson(int pcircle_notis_id) {
        try (Connection conn = sql2o.open()) {
            String sql = "select check_att,count(check_att) as count_chk from circle_notis WHERE pcircle_notis_id = :pcircle_notis_id GROUP BY check_att";
            return conn.createQuery(sql)
                    .addParameter("pcircle_notis_id",pcircle_notis_id)
                    .executeAndFetch(AttendCountDTO.class);
        }
    }
}
