package com.example.strommanager.retrofit;

import com.example.strommanager.model.user.User;
import com.example.strommanager.model.zaehlerstand.Zaehlerstand;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserApi {

    @GET("/user/get-all")
    Call<List<User>> getAllUser();

    @POST("/user/save")
    Call<User> save(@Body User user);


    @POST("/user/login")
    Call<User> login(@Body User user);

    @POST("zaehlerstand/save")
    Call<Zaehlerstand> saveZaehlerstand(@Body Zaehlerstand zaehlerstand);

}
