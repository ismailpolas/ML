package com.cubetiq.mlimagesearch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FacePredictClient {

  private static final String FACE_PREDICT_BASE_URL = "http://34.71.57.168:8000/";
  private static FacePredictClient retrofitClient;
  private Retrofit retrofit;

  private FacePredictClient() {

    Gson gson = new GsonBuilder()
        //.setLenient()
        .create();

    retrofit = new Retrofit.Builder()
        .baseUrl(FACE_PREDICT_BASE_URL)
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
