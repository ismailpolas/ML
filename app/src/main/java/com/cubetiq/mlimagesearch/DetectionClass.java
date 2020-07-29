package com.cubetiq.mlimagesearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetectionClass {

  @SerializedName("cow_id")
  @Expose
  public String cowId;
  @SerializedName("score")
  @Expose
  public Float score;
}
