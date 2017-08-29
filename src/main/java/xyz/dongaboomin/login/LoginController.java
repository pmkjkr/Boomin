package xyz.dongaboomin.login;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class LoginController {

    public BlockingQueue<String> login(String email, String password){
        BlockingQueue blockingQueue = new ArrayBlockingQueue(1);
        Retrofit client = new Retrofit.Builder().baseUrl("https://www.dongaboomin.xyz:20433/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Interface_login authLogin = client.create(Interface_login.class);
        Call<Master> call = authLogin.authLogin(email, password);
        call.enqueue(new Callback<Master>() {
            @Override
            public void onResponse(Call<Master> call, Response<Master> response) {
                blockingQueue.add(response.body().getToken());
            }

            @Override
            public void onFailure(Call<Master> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return blockingQueue;
    }
}
