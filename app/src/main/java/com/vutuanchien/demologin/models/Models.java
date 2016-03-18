package com.vutuanchien.demologin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MY PC on 16/03/2016.
 */
public class Models {
    @SerializedName("users")
    @Expose
    public List<User> users = new ArrayList<User>();
}
