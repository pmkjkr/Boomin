package xyz.dongaboomin.user.controller;

import org.sql2o.Sql2o;
import xyz.dongaboomin.user.BCrypt;
import xyz.dongaboomin.user.dao.UserDAO;
import xyz.dongaboomin.user.dto.UserDTO;
import xyz.dongaboomin.user.dao.UserModel;

import java.util.List;

public class UserController {

    private UserModel model;

    public UserController(Sql2o sql2o){
        model = new UserDAO(sql2o);
    }

    public Long reg(String email,String pw){
        String hashPw = BCrypt.hashpw(pw,BCrypt.gensalt());
        return model.reg(email,hashPw);
    }

    public UserDTO login(String email,String pw){
        return model.login(email);
    }

    public List<UserDTO> showUserList(int limit, int offset){
        return model.showUserList(limit, offset);
    }
}
