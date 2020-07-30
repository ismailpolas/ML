package com.cubetiq.mlimagesearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacePredictResponse {

  @SerializedName("message")
  @Expose
  public String message;
  @SerializedName("predictions")
  @Expose
  public Predictions predictions;

  @Override
  public String toString() {
    return "FacePredictResponse{" +
        "message='" + message + '\'' +
        ", predictions=" + predictions +
        '}';
  }
}
