package com.vutuanchien.demologin;

import com.vutuanchien.demologin.models.Models;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by MY PC on 16/03/2016.
 */
public interface API {

    @GET("/demo/users.json")
    void getData(Callback<Models> repon);
}
