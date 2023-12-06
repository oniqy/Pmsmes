package com.example.pmsmes.Utils;

import com.example.pmsmes.Models.Login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //PROJECT
    @GET("/api/project")
    Call<Object> getAllProjects(@Header("Authorization") String token);

    @GET("/api/project/{id}")
    Call<Object> getProjectByID(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project")
    Call<Object> createNewProject(@Header("Authorization") String token,@Field("name") String projectName, @Field("description") String description, @Field("creator") String creatorId);

    @DELETE("/api/project/{id}")
    Call<Object> deleteProject(@Header("Authorization") String token,@Path("id") String projectId);

    @GET("/api/project/{id}/members")
    Call<Object> getProjectMemberList(@Header("Authorization") String token, @Path("id") String projectId);

    @GET("/api/project/{id}/members/getUsers")
    Call<Object> getNotInProjectUserList(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/members")
    Call<Object> addMemberToProject(@Header("Authorization") String token, @Path("id") String projectId,@Field("user") String userID);


    @DELETE("/api/project/{id}/members")
    Call<Object> removeMemberFromProject(@Header("Authorization") String token, @Path("id") String projectId,@Body Object body);

    //Stage
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/project/{id}/stages")
    Call<Object> getProjectStages(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/stages")
    Call<Object> createNewStage(@Header("Authorization") String token, @Path("id") String projectId, @Field("name") String stageName);

    @PATCH("/api/project/{id}/stages")
    Call<Object> updateProjectStage(@Header("Authorization") String token, @Path("id") String projectId, @Body Object body);


    @HTTP(method = "DELETE", path = "/api/project/{id}/stages", hasBody = true )
    Call<Object> removeProjectStage(@Header("Authorization") String token, @Path("id") String projectId, @Body Object stageID);

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

    @GET("/api/project/{id}/task/{taskID}")
    Call<Object> getTaskByID(@Header("Authorization") String token,@Path("id") String projectId, @Path("taskID") String taskID);

//    @FormUrlEncoded
//    @DELETE("/api/project/{id}/tasks/")
    @HTTP(method = "DELETE", path = "/api/project/{id}/tasks/", hasBody = true )
    Call<Object> removeTask(@Header("Authorization") String token, @Path("id") String projectId,@Body Object taskToDeleteObject);


    //TAG
    @GET("/api/project/{id}/tags")
    Call<Object> getProjectTag(@Header("Authorization") String token, @Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/tags")
    Call<Object> createProjectTag(@Header("Authorization") String token, @Path("id") String projectId, @Field("name") String tagName, @Field("color") String colorHEX);


    @PATCH("/api/project/{id}/tags")
    Call<Object> updateProjectTag(@Header("Authorization") String token, @Path("id") String projectId, @Body Object body);

    @FormUrlEncoded
    @DELETE("/api/project/{id}/tags")
    Call<Object> removeProjectTag(@Header("Authorization") String token, @Path("id") String projectId, @Field("tag") String tagID);

    //User
    @GET("/api/users/{id}/project")
    Call<Object> getMyProject(@Header("Authorization") String token, @Path("id") String userId);

    @GET("/api/users/{id}")
    Call<Object> getUserById(@Header("Authorization") String token, @Path("id") String userId);


    //ACCOUNT
    @FormUrlEncoded
    @POST("/api/account/login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/account/forgotPassword")
    Call<Object> forgotPassword(@Field("email") String email);

    @GET("/api/account/logout")
    Call<Object> logout(@Header("Authorization") String token);


}
