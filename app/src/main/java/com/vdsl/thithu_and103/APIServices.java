package com.vdsl.thithu_and103;


import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {

    String DOMAIN = "http://192.168.17.10:3030/";

    // Get list of motorcycles
    @GET("/api/xemay")
    Call<List<XeMay>> getMotorcycles();

    // Get a motorcycle by ID
    @GET("/api/xemay/{id}")
    Call<XeMay> getMotorcycleDetail(@Path("id") String id);

    // Search motorcycles by name
    @GET("/api/xemay/search")
    Call<List<XeMay>> searchMotorcycles(@Query("q") String query);

    // Add a new motorcycle
    @POST("/api/xemay")
    Call<XeMay> addMotorcycle(@Body XeMay motorcycle);

    // Update an existing motorcycle
    @PUT("/xemay/{id}")
    Call<XeMay> updateMotorcycle(@Path("id") String id, @Body XeMay motorcycle);

    // Delete a motorcycle
    @DELETE("/api/xemay/{id}")
    Call<Void> deleteMotorcycle(@Path("id") String id);

    @Multipart
    @POST("/api/add-xemay-withImage")
    Call<XeMay> addMotorcycleWithImage(
            @Part("ten_xe_PH46164") RequestBody name,
            @Part("mau_sac_PH46164") RequestBody color,
            @Part("gia_ban_PH46164") RequestBody price,
            @Part("mo_ta_PH46164") RequestBody description,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("/api/xemay/{id}/withImage")
    Call<XeMay> updateMotorcycleWithImage(
            @Path("id") String id,
            @Part("ten_xe_PH46164") RequestBody name,
            @Part("mau_sac_PH46164") RequestBody color,
            @Part("gia_ban_PH46164") RequestBody price,
            @Part("mo_ta_PH46164") RequestBody description,
            @Part MultipartBody.Part image
    );
}


