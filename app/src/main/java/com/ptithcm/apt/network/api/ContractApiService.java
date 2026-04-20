package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.contract.ContractPageResponse;
import com.ptithcm.apt.models.contract.ContractResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ContractApiService {
    @GET("api/v1/admin/contracts")
    Call<com.ptithcm.apt.models.contract.ContractPageResponse> getAllContracts(
            @Query("keyword") String keyword,
            @Query("role") String role,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("api/v1/admin/contracts/{id}")
    Call<ContractResponse> getContractDetail(@Path("id") long id);
}