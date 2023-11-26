package com.example.pmsmes.API;

import com.example.pmsmes.ItemAdapter.Project;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface API {
    @GET("/api/project")
    Call<Project> PullAllProject(@Header("Authorization") String token);
    Call<Project> PullProject(@Header("Authorization") String token, String id);
}
