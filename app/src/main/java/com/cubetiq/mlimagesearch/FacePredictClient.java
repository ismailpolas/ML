package com.cubetiq.mlimagesearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacePredictClient {

  private static final String FACE_PREDICT_BASE_URL = "http://34.71.57.168:8000/";
  private static FacePredictClient retrofitClient;
  private Retrofit retrofit;

  private FacePredictClient() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.level(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .build();

    Gson gson = new GsonBuilder().create();

    retrofit = new Retrofit.Builder()
        .baseUrl(FACE_PREDICT_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
  }

  public static synchronized FacePredictClient getInstance() {
    if (retrofitClient == null) {
      retrofitClient = new FacePredictClient();
    }
    return retrofitClient;
  }

  public FacePredictEndPoint getApi() {
    return retrofit.create(FacePredictEndPoint.class);
  }
}
