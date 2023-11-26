package com.example.pmsmes.Utils;

import com.example.pmsmes.Models.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIInterface {

    //PROJECT
    @GET("/api/project")
    Call<Object> getAllProjects(@Header("Authorization") String token);

    @GET("/api/project/{id}")
    Call<Object> getProjectByID(@Header("Authorization") String token, @Path("id") String projectId);

    @GET("/api/project/{id}/members")
    Call<Object> getProjectMemberList(@Header("Authorization") String token, @Path("id") String projectId);

    //Stage
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/project/{id}/stages")
    Call<Object> getProjectStages(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/stages")
    Call<Object> createNewStage(@Header("Authorization") String token, @Path("id") String projectId, @Field("name") String stageName);

    @PATCH("/api/project/{id}/stages")
    Call<Object> updateProjectStage(@Header("Authorization") String token, @Path("id") String projectId, @Body Object body);

    //TASK
    @GET("/api/project/{id}/tasks")
    Call<Object> getProjectTask(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/tasks")
    Call<Object> createNewTask(@Header("Authorization") String token, @Path("id") String projectId, @Field("name") String taskName, @Field("creator") String creatorID, @Field("stage") String stageID);

    @PATCH("/api/project/{id}/tasks")
    Call<Object> updateProjectTask(@Header("Authorization") String token, @Path("id") String projectId, @Body Object body);

    @FormUrlEncoded
    @PATCH("/api/project/{id}/tasks/assign")
    Call<Object> assignTask(@Header("Authorization") String token, @Field("task") String taskID, @Field("assignee") String[] memberToAssignList);

//    @DELETE("/api/project/{id}/tasks/")
//    Call<Object> deleteTask(@Header("Authorization") String token,)

    //User



    //ACCOUNT
    @FormUrlEncoded
    @POST("/api/account/login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);

    @GET("/api/account/logout")
    Call<Object> logout(@Header("Authorization") String token);
}
