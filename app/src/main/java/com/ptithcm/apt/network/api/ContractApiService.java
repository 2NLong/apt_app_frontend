package com.ptithcm.apt.network.api;

import com.ptithcm.apt.models.contract.ContractPageResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContractApiService {
    @GET("api/v1/admin/contracts")
    Call<com.ptithcm.apt.models.contract.ContractPageResponse> getContracts(
            @Query("roomNumber") String roomNumber,
            @Query("page") int page,
            @Query("size") int size
    );
}