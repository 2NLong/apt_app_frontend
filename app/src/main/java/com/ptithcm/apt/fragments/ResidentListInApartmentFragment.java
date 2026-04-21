package com.ptithcm.apt.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.ResidentInApartmentAdapter;
import com.ptithcm.apt.models.resident.ResidentListResponse;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResidentListInApartmentFragment extends Fragment {

    private RecyclerView rvResidents;
    private TextView tvEmpty;
    private ResidentInApartmentAdapter adapter;
    private Long apartmentId = -1L;

    public ResidentListInApartmentFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_list_in_apartment, container, false);

        // 1. Nhận ID căn hộ từ màn hình Update
        if (getArguments() != null) {
            apartmentId = getArguments().getLong("APARTMENT_ID", -1L);
        }

        rvResidents = view.findViewById(R.id.rv_residents_in_apartment);
        tvEmpty = view.findViewById(R.id.tv_empty_residents);

        // 2. Setup RecyclerView
        rvResidents.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ResidentInApartmentAdapter(new ArrayList<>());
        rvResidents.setAdapter(adapter);

        // 3. Nút Back
        view.findViewById(R.id.toolbar_resident_list).setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // 4. Gọi API
        if (apartmentId != -1L) {
            fetchResidents(apartmentId);
        } else {
            Toast.makeText(getContext(), "Lỗi: Không lấy được ID phòng!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void fetchResidents(Long aptId) {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);

        apiService.getResidentsInApartment(aptId).enqueue(new Callback<List<ResidentListResponse>>() {
            @Override
            public void onResponse(Call<List<ResidentListResponse>> call, Response<List<ResidentListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ResidentListResponse> list = response.body();

                    if (list.isEmpty()) {
                        rvResidents.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        rvResidents.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        adapter.updateData(list);
                    }
                } else {
                    Toast.makeText(getContext(), "Không thể tải danh sách cư dân", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResidentListResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}