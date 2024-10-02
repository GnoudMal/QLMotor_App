package com.vdsl.thithu_and103;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMotorcycleActivity extends AppCompatActivity {

    private EditText editTextName, editTextPrice, editTextColor, editTextDetail;
    private ImageView imageView;
    private Button buttonAdd, buttonSelectImage;
    private ArrayList<File> imageFiles = new ArrayList<>();
    private HttpRequest httpRequest;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motorcycle);

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextColor = findViewById(R.id.editTextColor);
        editTextDetail = findViewById(R.id.editTextDetail);
        imageView = findViewById(R.id.imageView);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonSelectImage = findViewById(R.id.buttonSelectImage);

        httpRequest = new HttpRequest();

        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMotorcycle();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImage.launch(intent);
    }


    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imageFiles.clear();
                        Intent data = result.getData();
                        if (data != null) {
                            if (data.getData() != null) {
                                Uri imageUri = data.getData();
                                File file = createFileFromUri(imageUri, "image");
                                imageFiles.add(file);

                                if (!imageFiles.isEmpty()) {
                                    Glide.with(AddMotorcycleActivity.this)
                                            .load(imageFiles.get(0))
                                            .centerCrop()
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .into(imageView);
                                }
                            }
                        }
                    }
                }
            });


    private File createFileFromUri(Uri uri, String name) {
        File file = new File(getCacheDir(), name + ".png");
        try (InputStream in = getContentResolver().openInputStream(uri);
             OutputStream out = new FileOutputStream(file)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addMotorcycle() {
        String name = editTextName.getText().toString().trim();
        String priceString = editTextPrice.getText().toString().trim();
        String color = editTextColor.getText().toString().trim();
        String detail = editTextDetail.getText().toString().trim();

        if (name.isEmpty() || priceString.isEmpty() || color.isEmpty() || detail.isEmpty() || imageFiles.isEmpty()) {
            Toast.makeText(AddMotorcycleActivity.this, "Vui lòng điền đầy đủ thông tin và chọn một hình ảnh", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(AddMotorcycleActivity.this, "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody namePart = RequestBody.create(MultipartBody.FORM, name);
        RequestBody pricePart = RequestBody.create(MultipartBody.FORM, String.valueOf(price));
        RequestBody colorPart = RequestBody.create(MultipartBody.FORM, color);
        RequestBody detailPart = RequestBody.create(MultipartBody.FORM, detail);

        MultipartBody.Part imagePart = null;
        if (!imageFiles.isEmpty()) {
            File file = imageFiles.get(0);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            imagePart = MultipartBody.Part.createFormData("hinh_anh_PH46164", file.getName(), requestFile);
        }

        // Ghi log thông tin yêu cầu để debug
        Log.d("RequestPayload", "Tên: " + name);
        Log.d("RequestPayload", "Giá: " + price);
        Log.d("RequestPayload", "Màu sắc: " + color);
        Log.d("RequestPayload", "Chi tiết: " + detail);
        Log.d("RequestPayload", "Số lượng hình ảnh: " + imageFiles.size());

        httpRequest.callAPI().addMotorcycleWithImage(namePart, colorPart, pricePart, detailPart, imagePart).enqueue(new Callback<XeMay>() {
            @Override
            public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AddMotorcycleActivity.this, "Xe máy đã được thêm thành công", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);  // Đánh dấu thành công
                    finish();  // Đóng activity sau khi thêm thành công
                } else {
                    Log.e("AddMotorcycleActivity", "Phản hồi không thành công: " + response.code());
                    Toast.makeText(AddMotorcycleActivity.this, "Thêm xe máy không thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<XeMay> call, Throwable t) {
                Log.e("AddMotorcycleActivity", "Thêm xe máy không thành công", t);
                Toast.makeText(AddMotorcycleActivity.this, "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
