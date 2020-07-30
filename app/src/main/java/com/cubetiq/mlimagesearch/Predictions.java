package com.cubetiq.mlimagesearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Predictions {

  @SerializedName("detection_classes")
  @Expose
  public List<DetectionClass> detectionClasses = null;

  @Override
  public String toString() {
    return "Predictions{" +
        "detectionClasses=" + detectionClasses +
        '}';
  }
}
