package com.cubetiq.mlimagesearch;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FacePredictEndPoint {

  @Multipart
  @POST("api/v1/face_predict/")
  Call<FacePredictResponse> facePredict(@Part MultipartBody.Part file);

  @Multipart
  @POST("api/v1/img_classify/")
  Call<FacePredictResponse> imgClassify(
          @Part MultipartBody.Part file,
          @Part("farm_id") RequestBody farmId);
}
