package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.complaint.ComplaintResponse;
import com.ptithcm.apt.models.complaint.CreateComplaintRequest;
import com.ptithcm.apt.models.complaint.UpdateComplaintStatusRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ComplaintApiService {
    @GET("api/v1/complaints")
    Call<ApiResponse<List<ComplaintResponse>>> getAllComplaints();

    @GET("api/v1/complaints/my")
    Call<ApiResponse<List<ComplaintResponse>>> getMyComplaints();

    @POST("api/v1/complaints")
    Call<ApiResponse<ComplaintResponse>> createComplaint(@Body CreateComplaintRequest request);

    @PATCH("api/v1/complaints/{id}/status")
    Call<ApiResponse<ComplaintResponse>> updateStatus(
            @Path("id") Long id,
            @Body UpdateComplaintStatusRequest request
    );
}
