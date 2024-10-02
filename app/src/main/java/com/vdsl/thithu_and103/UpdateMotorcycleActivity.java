package com.vdsl.thithu_and103;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMotorcycleActivity extends AppCompatActivity {
    private EditText editTextName, editTextColor, editTextPrice, editTextDescription;
    private ImageView imageViewMotorcycle;
    private Button buttonUpdate;
    private String motorcycleId;
    private Uri selectedImageUri;
    private APIServices apiServices;
    private static final String TAG = "UpdateMotorcycleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_motorcycle);


        editTextName = findViewById(R.id.editTextName);
        editTextColor = findViewById(R.id.editTextColor);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDescription = findViewById(R.id.editTextDescription);
        imageViewMotorcycle = findViewById(R.id.imageViewMotorcycle);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        apiServices = new HttpRequest().callAPI(); // Use HttpRequest to create API services

        // Get the motorcycle ID from the intent
        motorcycleId = getIntent().getStringExtra("motorcycle_id");
        Log.d(TAG, "Motorcycle ID: " + motorcycleId);
        // Load motorcycle details
        loadMotorcycleDetails(motorcycleId);

        // Set image selection listener
        imageViewMotorcycle.setOnClickListener(v -> selectImage());

        // Set update button listener
        buttonUpdate.setOnClickListener(v -> updateMotorcycle());
    }

    private void loadMotorcycleDetails(String id) {
        Log.d(TAG, "Loading details for motorcycle ID: " + id);
        apiServices.getMotorcycleDetail(id).enqueue(new Callback<XeMay>() {
            @Override
            public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                if (response.isSuccessful() && response.body() != null) {
                    XeMay motorcycle = response.body();
                    Log.d(TAG, "Motorcycle details loaded: " + motorcycle.toString());
                    editTextName.setText(motorcycle.getTen_xe_PH46164());
                    editTextColor.setText(motorcycle.getMau_sac_PH46164());
                    editTextPrice.setText(String.valueOf(motorcycle.getGia_ban_PH46164()));
                    editTextDescription.setText(motorcycle.getMo_ta_PH46164());
                    Glide.with(UpdateMotorcycleActivity.this)
                            .load(motorcycle.getHinh_anh_PH46164())
                            .into(imageViewMotorcycle);
                } else {
                    Log.d(TAG, "Failed to load motorcycle details. Response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<XeMay> call, Throwable t) {
                Log.e(TAG, "Error loading motorcycle details", t);
            }
        });
    }

    private void selectImage() {
        Log.d(TAG, "Selecting image...");
        // Open gallery to select an image
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            Log.d(TAG, "Image selected: " + selectedImageUri.toString());
            imageViewMotorcycle.setImageURI(selectedImageUri);
        }
    }

    private void updateMotorcycle() {
        Log.d(TAG, "Updating motorcycle...");

        String name = editTextName.getText().toString();
        String color = editTextColor.getText().toString();
        String price = editTextPrice.getText().toString();
        String description = editTextDescription.getText().toString();

        // Create a RequestBody for each field
        RequestBody nameBody = RequestBody.create(MultipartBody.FORM, name);
        RequestBody colorBody = RequestBody.create(MultipartBody.FORM, color);
        RequestBody priceBody = RequestBody.create(MultipartBody.FORM, price);
        RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);

        // Create a MultipartBody.Part for the image
        MultipartBody.Part imagePart = null;
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                if (inputStream != null) {
                    File file = new File(getCacheDir(), "image.png");
                    OutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.flush();
                    outputStream.close();
                    inputStream.close();

                    RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), file);
                    imagePart = MultipartBody.Part.createFormData("hinh_anh_PH46164", file.getName(), imageBody);
                    Log.d(TAG, "Image file created: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                Log.e(TAG, "File error", e);
            }
        }

        // Make the API call to update the motorcycle
        Call<XeMay> call = apiServices.updateMotorcycleWithImage(
                motorcycleId, nameBody, colorBody, priceBody, descriptionBody, imagePart
        );
        call.enqueue(new Callback<XeMay>() {
            @Override
            public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UpdateMotorcycleActivity.this, "Xe máy đã được sửa thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Đánh dấu thành công
                    finish();  // Đóng activity sau khi thêm thành công
                } else {
                    Log.d(TAG, "Failed to update motorcycle. Response: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<XeMay> call, Throwable t) {
                Log.e(TAG, "Error updating motorcycle", t);
            }
        });
    }


    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            Log.d(TAG, "Real path from URI: " + path);
            return path;
        }
        Log.d(TAG, "Failed to get real path from URI.");
        return null;
    }
}
