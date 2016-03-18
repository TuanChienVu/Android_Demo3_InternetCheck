package com.vutuanchien.demologin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by MY PC on 17/03/2016.
 */
public class User {

    @SerializedName("user")
    @Expose
    public String user;
    @SerializedName("pass")
    @Expose
    public String pass;
}
