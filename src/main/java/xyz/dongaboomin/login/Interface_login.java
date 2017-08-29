package xyz.dongaboomin.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Interface_login {
    @FormUrlEncoded
    @POST("/auth/login")
    Call<xyz.dongaboomin.login.Master> authLogin(@Field("email") String email, @Field("password") String password);
}