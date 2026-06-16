package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.resident.MemberRequest;
import com.ptithcm.apt.models.resident.Resident;
import com.ptithcm.apt.models.resident.ResidentDetailResponse;
import com.ptithcm.apt.models.resident.ResidentListResponse;
import com.ptithcm.apt.models.resident.ResidentPageResponse;
import com.ptithcm.apt.models.resident.UpdateResidentRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ResidentApiService {

    @POST("api/v1/residents/apartments/{roomNumber}/members")
    Call<ResidentListResponse> addMemberToApartment(
            @Path("roomNumber") String roomNumber,
            @Body MemberRequest request
    );
    @GET("api/v1/residents")
    Call<ResidentPageResponse> getActiveResidents(
            @Query("keyword") String keyword,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/v1/residents/apartment/{apartmentId}")
    Call<List<ResidentListResponse>> getResidentsByApartment(@Path("apartmentId") Long apartmentId);
    @PUT("api/v1/residents/{residentId}/apartments/{apartmentId}/move-out")
    Call<Void> moveOutResident(
            @Path("residentId") Long residentId,
            @Path("apartmentId") Long apartmentId
    );

    @PUT("api/v1/residents/{id}")
    Call<ResidentDetailResponse> updateResident(
            @Path("id") Long id,
            @Body UpdateResidentRequest request
    );

    @GET("api/v1/residents/{id}")
    Call<ResidentDetailResponse> getResidentDetail(@Path("id") Long id);

    //Lấy cư dân sống trong 1 căn hộ
    @GET("api/v1/residents/apartments/{apartmentId}")
    Call<List<ResidentListResponse>> getResidentsInApartment(@Path("apartmentId") Long apartmentId);

    @GET("api/v1/residents/check/{cccd}")
    Call<Resident> checkResidentByCccd(@Path("cccd") String cccd);
}
