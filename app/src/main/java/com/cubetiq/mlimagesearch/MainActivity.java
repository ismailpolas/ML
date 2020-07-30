package com.cubetiq.mlimagesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";

  Button submit, ChooseButton;
  TextView data;
  ImageView SelectImage;
  EditText farmId;


  Uri FilePathUri;
  File imageFile;

  // Image request code for onActivityResult() .
  int Image_Request_Code = 7;

  ProgressDialog progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    submit = (Button) findViewById(R.id.button);
    data = (TextView) findViewById(R.id.fetchdata);

    ChooseButton = (Button) findViewById(R.id.ButtonChooseImage);
    SelectImage = (ImageView) findViewById(R.id.ShowImageView);
    farmId = findViewById(R.id.farmIdEditText);

    progressDialog = new ProgressDialog(MainActivity.this);
    progressDialog.setMessage("Please wait...");

    ChooseButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

      }
    });

    submit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (imageFile == null) {
          Toast.makeText(MainActivity.this, "Please choose an image to upload",
              Toast.LENGTH_SHORT).show();
          return;
        }

        if (farmId.getText().toString().isEmpty()) {
          Toast.makeText(MainActivity.this, "Please enter a farm id",
              Toast.LENGTH_SHORT).show();
          return;
        }

        progressDialog.show();

        MultipartBody.Part part = null;
        try {
          RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
          part = MultipartBody.Part.createFormData("image", imageFile.getName(), fileBody);
        } catch (Exception e) {
          e.printStackTrace();
        }

        FacePredictClient.getInstance().getApi().imgClassify(
            part,
            RequestBody.create(farmId.getText().toString(), MediaType.parse("text/plain")))
            .enqueue(new Callback<FacePredictResponse>() {
              @Override
              public void onResponse(Call<FacePredictResponse> call, Response<FacePredictResponse> response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: response.body() = " + response.body());
                if (response.isSuccessful()) {
                  StringBuilder result = new StringBuilder();
                  data.setText(response.body().toString());
                  if (response.body().predictions != null) {
                    result.append("Cow Id: " + response.body().predictions.detectionClasses.get(0).cowId +
                        "\nScore: " + response.body().predictions.detectionClasses.get(0).score);
                    data.setText(result);
                  } else {
                    data.setText(response.body().message);
                  }
                } else {
                  data.setText("Something went wrong!");
                }
              }

              @Override
              public void onFailure(Call<FacePredictResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: t.getMessage() = " + t.getMessage(), t);
                data.setText("Unable to get data from server!");
              }
            });
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {
      FilePathUri = data.getData();
      Log.d(TAG, "onActivityResult: FilePathUri = " + FilePathUri);

      try {
        /*InputStream in = getContentResolver().openInputStream(FilePathUri);
        OutputStream out = new FileOutputStream(imageFile);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.close();
        in.close();*/
      } catch (Exception e) {
        e.printStackTrace();
      }

      try {
        // Getting selected image into Bitmap.
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
        // Setting up bitmap selected image into ImageView.
        SelectImage.setImageBitmap(bitmap);

        File filesDir = getFilesDir();
        imageFile = new File(filesDir, "image_to_upload.jpg");

        OutputStream os;
        try {
          os = new FileOutputStream(imageFile);
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
          os.flush();
          os.close();
        } catch (Exception e) {
          Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        Log.d(TAG, "onActivityResult: imageFile = " + imageFile.toString());
        ChooseButton.setText("Image Selected");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public String GetFileExtension(Uri uri) {
    ContentResolver contentResolver = getContentResolver();
    MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
    // Returning the file Extension.
    return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
  }
}
