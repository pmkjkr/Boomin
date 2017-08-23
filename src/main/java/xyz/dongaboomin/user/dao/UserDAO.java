package xyz.dongaboomin.user.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import xyz.dongaboomin.user.dto.UserDTO;

import java.util.List;

public class UserDAO implements UserModel{

    private Sql2o sql2o;

    public UserDAO(Sql2o sql2o){
        this.sql2o = sql2o;
    }

    @Override
    public UserDTO login(String email) {
        try (Connection conn = sql2o.open()) {
            String sql = "select * from users where email = :email";
            return conn.createQuery(sql).addParameter("email",email).executeAndFetch(UserDTO.class).get(0);
        }
    }

    @Override
    public long reg(String email, String pw) {
        try (Connection conn = sql2o.open()) {
            String sql = "insert into users(email,password,created_at,updated_at) values (:email, :password, sysdate(),sysdate())";
            return (long) conn.createQuery(sql, true)
                    .addParameter("email",email)
                    .addParameter("password",pw)
                    .executeUpdate()
                    .getKey();
        }
    }

    @Override
    public List<UserDTO> showUserList(int limit, int offset){
        try (Connection conn = sql2o.open()) {
            String sql = "select * from users limit :limit offset :offset";
            return conn.createQuery(sql)
                    .addParameter("limit",limit)
                    .addParameter("offset",offset)
                    .executeAndFetch(UserDTO.class);
        }
    }
}
