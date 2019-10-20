package hu.bme.aut.adminclient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitLogin {

    @GET("login/admin")
    Call<String> tryLogin(@Header("Authorization") String authHeader);
}
