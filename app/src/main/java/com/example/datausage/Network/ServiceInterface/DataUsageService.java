package com.example.datausage.Network.ServiceInterface;

import com.example.datausage.Network.Model.DataUsage;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DataUsageService {

    @GET("api/action/datastore_search")
    Call<DataUsage> listRepos(@Query("resource_id") String resourceId);
}
