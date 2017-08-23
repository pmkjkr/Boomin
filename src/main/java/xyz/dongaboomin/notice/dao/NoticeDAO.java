package xyz.dongaboomin.notice.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import xyz.dongaboomin.notice.dto.NoticeDTO;


import java.util.List;

public class NoticeDAO implements NoticeModel {
    private Sql2o sql2o;

    public NoticeDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public List<NoticeDTO> showNoticeList() {
        try (Connection conn = sql2o.open()) {
            String sql = "select * from publicNotice";
            return conn.createQuery(sql).executeAndFetch(NoticeDTO.class);
        }
    }

    @Override
    public long writeNotice(String title, String contents) {
        try (Connection conn = sql2o.open()) {
            String sql = "insert into publicNotice(title,contents,created_at,updated_at) values (:title, :contents, sysdate(),sysdate())";
            return (long) conn.createQuery(sql, true)
                    .addParameter("title",title)
                    .addParameter("contents",contents)
                    .executeUpdate()
                    .getKey();
        }
    }
}
