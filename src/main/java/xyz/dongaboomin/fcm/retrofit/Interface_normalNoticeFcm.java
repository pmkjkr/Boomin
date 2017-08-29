package xyz.dongaboomin.fcm.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Interface_normalNoticeFcm {
    @POST("/normal/fcm")
    Call<Master> sendNormalFcm(@Header("Authorization") String token, @Header("Content-Type") String appjson, @Body Article article);
}
