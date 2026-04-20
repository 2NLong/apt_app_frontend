package com.ptithcm.apt.network.api;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ResidentApiService {
    @POST("api/v1/admin/residents/{residentId}/move-out")
    Call<Void> moveOutResident(
            @Path("residentId") Long residentId,
            @Query("apartmentId") Long apartmentId
    );
}
