package xyz.dongaboomin.fcm.retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Interface_circleNoticeFcm {
//    @FormUrlEncoded
    @POST("/circle/fcm")
//    Call<Master> sendFcm(@Header("Authorization") String token, @Header("Content-Type") String appjson, @Field("title") String title,
//                         @Field("body") String body, @Field("contents") String contents);
    Call<Master> sendCircleFcm(@Header("Authorization") String token, @Header("Content-Type") String appjson, @Body Article article);
}
