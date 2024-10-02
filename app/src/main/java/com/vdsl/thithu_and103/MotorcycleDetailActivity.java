package com.vdsl.thithu_and103;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorcycleDetailActivity extends AppCompatActivity {
    private ImageView imageViewMotorcycleDetail;
    private TextView textViewNameDetail, textViewPriceDetail, textViewColorDetail;
    private HttpRequest httpRequest;
    private String motorcycleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorcycle_detail);

        imageViewMotorcycleDetail = findViewById(R.id.imageViewMotorcycleDetail);
        textViewNameDetail = findViewById(R.id.textViewNameDetail);
        textViewPriceDetail = findViewById(R.id.textViewPriceDetail);
        textViewColorDetail = findViewById(R.id.textViewColorDetail);

        httpRequest = new HttpRequest();

        motorcycleId = getIntent().getStringExtra("motorcycle_id");
        Log.e("check id", "onCreate: " + motorcycleId );
        if (motorcycleId == null) {
            Log.e("MotorcycleDetailActivity", "Motorcycle ID is null");
        }

        fetchMotorcycleDetail();
    }

    private void fetchMotorcycleDetail() {
        httpRequest.callAPI().getMotorcycleDetail(motorcycleId).enqueue(new Callback<XeMay>() {
            @Override
            public void onResponse(Call<XeMay> call, Response<XeMay> response) {
                if (response.isSuccessful()) {
                    XeMay motorcycle = response.body();
                    if (motorcycle != null) {
                        textViewNameDetail.setText(motorcycle.getTen_xe_PH46164());
                        textViewPriceDetail.setText(String.format("%.2f VNĐ", motorcycle.getGia_ban_PH46164()));
                        textViewColorDetail.setText(motorcycle.getMau_sac_PH46164()); // Đã sửa từ mo_ta_PH46164
                        Glide.with(MotorcycleDetailActivity.this).load(motorcycle.getHinh_anh_PH46164()).into(imageViewMotorcycleDetail);
                    } else {
                        Log.e("MotorcycleDetailActivity", "Motorcycle object is null");
                    }
                } else {
                    Log.e("MotorcycleDetailActivity", "Response not successful: " + response.code());
                }
            }


            @Override
            public void onFailure(Call<XeMay> call, Throwable t) {
                Log.e("MotorcycleDetailActivity", "Failed to fetch motorcycle details", t);
            }
        });
    }
}
