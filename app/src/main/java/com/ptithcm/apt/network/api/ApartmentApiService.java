package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.models.apartment.ApartmentPageResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApartmentApiService {
    // API Lấy danh sách có phân trang
    @GET("api/v1/apartments")
    Call<ApartmentPageResponse> getApartmentsByPage(@Query("page") int page);

    // API Tìm kiếm (Giả định URL Controller của bạn, hãy sửa lại cho khớp nếu cần)
    @GET("api/v1/apartments/search")
    Call<List<Apartment>> searchApartments(@Query("keyword") String keyword);

    @GET("api/v1/apartments/{id}")
    Call<Apartment> getApartmentById(@Path("id") Long id);

    // Lọc theo trạng thái
    @GET("api/v1/apartments/status/{status}")
    Call<List<Apartment>> getApartmentsByStatus(@Path("status") String status);

    @PUT("api/v1/apartments/{id}")
    Call<Apartment> updateApartment(@Path("id") Long id,@Body Apartment apartment);

    @POST("api/v1/apartments")
    Call<Apartment> createApartment(@Body Apartment apartment);
}
