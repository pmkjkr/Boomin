package xyz.dongaboomin.fcm.controller;

import org.sql2o.Sql2o;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import xyz.dongaboomin.fcm.dao.FcmDAO;
import xyz.dongaboomin.fcm.dao.FcmModel;
import xyz.dongaboomin.fcm.dto.CircleFcmDTO;
import xyz.dongaboomin.fcm.dto.NormalFcmDTO;
import xyz.dongaboomin.fcm.retrofit.*;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FcmController {
    private FcmModel model;

    public FcmController(Sql2o sql2o){
        model = new FcmDAO(sql2o);
    }

    public List<NormalFcmDTO> showNormalNotice(int limit, int offset, int admin_id){
        return model.showNormalNotice(limit, offset, admin_id);
    }

    public List<CircleFcmDTO> showCircleNotice(int limit, int offset, int admin_id){
        return model.showCircleNotice(limit, offset, admin_id);
    }

    public Long countNNTable(int admin_id){
        return model.countNNTable(admin_id);
    }

    public Long countCNTable(int admin_id){
        return model.countCNTable(admin_id);
    }

    public BlockingQueue<String> circleNoticeFcm(String authToken, String title, String body, String contents){
        BlockingQueue blockingQueue = new ArrayBlockingQueue(1);
        Retrofit client = new Retrofit.Builder().baseUrl("https://www.dongaboomin.xyz:20433/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Interface_circleNoticeFcm circleNoticeFcm = client.create(Interface_circleNoticeFcm.class);
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setTitle(title);
        articleDetail.setBody(body);
        articleDetail.setContents(contents);
        Article article = new Article();
        article.setArticle(articleDetail);
        Call<Master> call = circleNoticeFcm.sendCircleFcm("bearer "+authToken, "application/json", article);
        call.enqueue(new Callback<Master>() {
            @Override
            public void onResponse(Call<Master> call, Response<Master> response) {
                blockingQueue.add(response.body().getResult_code());
            }

            @Override
            public void onFailure(Call<Master> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return blockingQueue;
    }

    public BlockingQueue<String> normalNoticeFcm(String authToken, String title, String body, String contents){
        BlockingQueue blockingQueue = new ArrayBlockingQueue(1);
        Retrofit client = new Retrofit.Builder().baseUrl("https://www.dongaboomin.xyz:20433/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Interface_normalNoticeFcm normalNoticeFcm = client.create(Interface_normalNoticeFcm.class);
        ArticleDetail articleDetail = new ArticleDetail();
        articleDetail.setTitle(title);
        articleDetail.setBody(body);
        articleDetail.setContents(contents);
        Article article = new Article();
        article.setArticle(articleDetail);
        Call<Master> call = normalNoticeFcm.sendNormalFcm("bearer "+authToken, "application/json", article);
        call.enqueue(new Callback<Master>() {
            @Override
            public void onResponse(Call<Master> call, Response<Master> response) {
                blockingQueue.add(response.body().getResult_code());
            }

            @Override
            public void onFailure(Call<Master> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        return blockingQueue;
    }

}
