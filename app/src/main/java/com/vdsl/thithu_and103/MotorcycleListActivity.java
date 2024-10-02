package com.vdsl.thithu_and103;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorcycleListActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD_MOTORCYCLE = 1;
    private static final int REQUEST_CODE_UPDATE_MOTORCYCLE = 2;
    private RecyclerView recyclerView;
    private MotorcycleAdapter adapter;
    private List<XeMay> motorcycleList = new ArrayList<>();
    private HttpRequest httpRequest;
    private EditText editTextSearch;
    private FloatingActionButton btnAddProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motorcycle_list);

        recyclerView = findViewById(R.id.recyclerViewMotorcycles);
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        btnAddProduct = findViewById(R.id.fabAddMotor);
        adapter = new MotorcycleAdapter(this, motorcycleList, motorcycle -> {
            Intent intent = new Intent(MotorcycleListActivity.this, MotorcycleDetailActivity.class);
            intent.putExtra("motorcycle_id", motorcycle.get_id());
            startActivity(intent);
        }, motorcycle -> {
                    // Navigate to the update screen
                    Intent intent = new Intent(MotorcycleListActivity.this, UpdateMotorcycleActivity.class);
                    intent.putExtra("motorcycle_id", motorcycle.get_id());
                    startActivityForResult(intent, REQUEST_CODE_UPDATE_MOTORCYCLE);
                },
                motorcycle -> {
                    showDeleteConfirmationDialog(motorcycle.get_id());
                });
        recyclerView.setAdapter(adapter);

        httpRequest = new HttpRequest();
        fetchMotorcycles();

        findViewById(R.id.fabAddMotor).setOnClickListener(view -> {
            Intent intent = new Intent(MotorcycleListActivity.this, AddMotorcycleActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ADD_MOTORCYCLE);
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMotorcycles(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchMotorcycles() {
        httpRequest.callAPI().getMotorcycles().enqueue(new Callback<List<XeMay>>() {
            @Override
            public void onResponse(Call<List<XeMay>> call, Response<List<XeMay>> response) {
                Log.d("MotorcycleListActivity", "Response code: " + response.code());
                if (response.isSuccessful()) {
                    List<XeMay> result = response.body();
                    Log.d("MotorcycleListActivity", "Fetched results: " + (result != null ? result.size() : "null"));
                    if (result != null) {
                        motorcycleList.clear();
                        motorcycleList.addAll(result);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("MotorcycleListActivity", "Response not successful fetch: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<XeMay>> call, Throwable t) {
                Log.e("MotorcycleListActivity", "Failed to fetch motorcycles", t);
            }
        });
    }

    private void showDeleteConfirmationDialog(String motorcycleId) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete this motorcycle?")
                .setPositiveButton("Yes", (dialog, which) -> deleteMotorcycle(motorcycleId))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteMotorcycle(String id) {
        httpRequest.callAPI().deleteMotorcycle(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MotorcycleListActivity.this, "Motorcycle deleted successfully", Toast.LENGTH_SHORT).show();
                    fetchMotorcycles(); // Refresh the list
                } else {
                    Log.e("MotorcycleListActivity", "Failed to delete motorcycle: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MotorcycleListActivity", "Error deleting motorcycle", t);
            }
        });
    }



    private void searchMotorcycles(String query) {
        if (query.isEmpty()) {
            fetchMotorcycles();
        } else {
            // Nếu có chuỗi tìm kiếm, thực hiện tìm kiếm
            httpRequest.callAPI().searchMotorcycles(query).enqueue(new Callback<List<XeMay>>() {
                @Override
                public void onResponse(Call<List<XeMay>> call, Response<List<XeMay>> response) {
                    Log.d("MotorcycleListActivity", "Response code: " + response.code());
                    if (response.isSuccessful()) {
                        List<XeMay> result = response.body();
                        Log.d("MotorcycleListActivity", "Search results: " + (result != null ? result.size() : "null"));
                        if (result != null) {
                            motorcycleList.clear();
                            motorcycleList.addAll(result);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("MotorcycleListActivity", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<XeMay>> call, Throwable t) {
                    Log.e("MotorcycleListActivity", "Failed to search motorcycles", t);
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_ADD_MOTORCYCLE || requestCode == REQUEST_CODE_UPDATE_MOTORCYCLE) {
                fetchMotorcycles();
            }
        }
    }


}

