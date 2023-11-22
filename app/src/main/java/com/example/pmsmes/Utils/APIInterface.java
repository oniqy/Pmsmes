package com.example.pmsmes.Utils;

import com.example.pmsmes.Models.CreateNewTask;
import com.example.pmsmes.Models.GetProjectByID;
import com.example.pmsmes.Models.GetProjectMemberList;
import com.example.pmsmes.Models.GetProjectStages;
import com.example.pmsmes.Models.GetProjectTask;
import com.example.pmsmes.Models.Login;
import com.example.pmsmes.Models.Project;
import com.example.pmsmes.Models.ProjectResponse;
import com.example.pmsmes.Models.UpdateProjectTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<ProjectResponse> getAllProjects(@Header("Authorization") String token);

    @GET("/api/project/{id}")
    Call<GetProjectByID> getProjectByID(@Path("id") String projectId);

    @GET("/api/project/{id}/members")
    Call<GetProjectMemberList> getProjectMemberList(@Path("id") String projectId);

    //Stage
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("/api/project/{id}/stages")
    Call<GetProjectStages> getProjectStages(@Header("Authorization") String token, @Path("id") String projectId);

    //TASK
    @GET("/api/project/{id}/tasks")
    Call<GetProjectTask> getProjectTask(@Path("id") String projectId);

    @FormUrlEncoded
    @POST("/api/project/{id}/tasks")
    Call<CreateNewTask> createNewTask(@Path("id") String projectId, @Field("name") String taskName, @Field("creator") String creatorID, @Field("stage") String stageID);

    @PATCH("/api/project/{id}/tasks")
    Call<UpdateProjectTask> updateProjectTask(@Path("id") String projectId, @Body Object body);


    //User



    //ACCOUNT
    @FormUrlEncoded
    @POST("/api/account/login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);
}
