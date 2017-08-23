package xyz.dongaboomin.user.dao;

import xyz.dongaboomin.user.dto.UserDTO;

import java.util.List;

public interface UserModel {
    UserDTO login(String email);
    long reg(String email,String pw);
    List<UserDTO> showUserList(int limit, int offset);
}
