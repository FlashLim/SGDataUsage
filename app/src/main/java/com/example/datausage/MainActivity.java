package com.example.datausage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.datausage.DataModel.AnnualDataUsage;
import com.example.datausage.Network.Model.DataUsage;
import com.example.datausage.Network.Model.DataUsageRecord;
import com.example.datausage.Network.ServiceInterface.DataUsageService;
import com.example.datausage.Util.Helper;
import com.example.datausage.ViewAdapter.DataUsageAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataUsageAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<AnnualDataUsage> records;
    private DataUsageService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        File httpCacheDirectory = new File(getCacheDir(), "offlineCache");

        //10 MB
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(provideCacheInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl("https://data.gov.sg/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(DataUsageService.class);
        getResponse(service);
    }

    private synchronized void getResponse(DataUsageService service) {
        Call<DataUsage> repos = service.listRepos("a807b7ab-6cad-4aa6-87d0-e283a7353a0f");
        repos.enqueue(new Callback<DataUsage>() {
            @Override
            public void onResponse(Call<DataUsage> call, Response<DataUsage> response) {
                if (response.isSuccessful()) {
                    switch(response.code()) {
                        case 200:
                            if (response.body().isSuccess()) {
                                DataUsage dataUsage = response.body();
                                List<DataUsageRecord> dataUsageRecords = dataUsage.getResult().getRecords();
                                if (dataUsageRecords != null && dataUsageRecords.size() > 0) {
                                    records = Helper.filterRecords(dataUsageRecords);

                                    mAdapter = new DataUsageAdapter(records,MainActivity.this);
                                    recyclerView.setAdapter(mAdapter);
                                }
                            }
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), "Error: "+ String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<DataUsage> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private Interceptor provideCacheInterceptor() {

        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                okhttp3.Response originalResponse = chain.proceed(request);
                String cacheControl = originalResponse.header("Cache-Control");

                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {

                    CacheControl cc = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cc)
                            .build();

                    return chain.proceed(request);

                } else {
                    return originalResponse;
                }
            }
        };

    }


    private Interceptor provideOfflineCacheInterceptor() {

        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Exception e) {

                    CacheControl cacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();

                    Request offlineRequest = chain.request().newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                    return chain.proceed(offlineRequest);
                }
            }
        };
    }

}
