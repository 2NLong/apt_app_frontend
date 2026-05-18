package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.contract.ContractPageResponse;
import com.ptithcm.apt.models.contract.ContractRequest;
import com.ptithcm.apt.models.contract.ContractResponse;
import com.ptithcm.apt.models.resident.Resident;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContractApiService {
    @GET("api/v1/contracts")
    Call<com.ptithcm.apt.models.contract.ContractPageResponse> getAllContracts(
            @Query("keyword") String keyword,
            @Query("role") String role,
            @Query("page") int page,
            @Query("size") int size
    );

    @POST("api/v1/contracts")
    Call<Resident> createContract(@Body ContractRequest request);

    @GET("api/v1/contracts/{id}")
    Call<ContractResponse> getContractDetail(@Path("id") long id);
}