package com.ptithcm.apt.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.contract.ContractAdapter;
import com.ptithcm.apt.models.contract.ContractPageResponse;
import com.ptithcm.apt.network.api.ContractApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageContractFragment extends Fragment {

    private RecyclerView rvContracts;
    private EditText edtSearch;
    private Button btnPrev, btnNext;
    private TextView tvPageInfo;

    private Spinner searchSpinnerRole;
    private FloatingActionButton fabAddContract;

    private com.ptithcm.apt.adapters.contract.ContractAdapter adapter;
    private int currentPage = 0;
    private int totalPages = 1;
    private String currentKeyword = null;
    private String currentRole = null; // null là tất cả, "TENANT" hoặc "OWNER"
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    public ManageContractFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_contract, container, false);

        searchSpinnerRole = view.findViewById(R.id.spinner_filter_contract);
        List<String> roles = new ArrayList<>();
        roles.add("Tất cả");
        roles.add("TENANT");
        roles.add("OWNER");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                roles
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        searchSpinnerRole.setAdapter(spinnerAdapter);
        rvContracts = view.findViewById(R.id.rv_contracts);
        edtSearch = view.findViewById(R.id.edt_search_contract);
        btnPrev = view.findViewById(R.id.btn_prev_page_contract);
        btnNext = view.findViewById(R.id.btn_next_page_contract);
        tvPageInfo = view.findViewById(R.id.tv_page_info_contract);
        fabAddContract = view.findViewById(R.id.fab_add_contract);


        rvContracts.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContractAdapter(new ArrayList<>(), contract -> {
            Bundle bundle = new Bundle();
            bundle.putLong("CONTRACT_ID", contract.getId());

            ContractDetailFragment detailFragment = new ContractDetailFragment();
            detailFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,detailFragment).addToBackStack(null).commit();
        });
        rvContracts.setAdapter(adapter);

        fabAddContract.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Vui lòng vào Chi tiết phòng để thêm hợp đồng!", Toast.LENGTH_LONG).show();
        });

        // 3. Setup Events
        setupPaginationListeners();
        setupSearchListener();
        setupRoleFilterListener();

        // 4. Lấy dữ liệu lần đầu (Trang 0)
        fetchContracts(currentKeyword,currentRole, 0);

        return view;
    }

    // HÀM GỌI API
    private void fetchContracts(String keyword,String role, int page) {
        ContractApiService apiService = RetrofitClient.getInstance().createService(ContractApiService.class);
        apiService.getAllContracts(keyword,role, page, 5).enqueue(new Callback<ContractPageResponse>() {
            @Override
            public void onResponse(Call<ContractPageResponse> call, Response<ContractPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ContractPageResponse pageResponse = response.body();

                    // Cập nhật dữ liệu cho Adapter
                    adapter.setData(pageResponse.getContent());

                    // Cập nhật phân trang
                    totalPages = pageResponse.getTotalPages();
                    currentPage = pageResponse.getNumber();

                    tvPageInfo.setText((currentPage + 1) + " / " + Math.max(1, totalPages));
                    btnPrev.setEnabled(currentPage > 0);
                    btnNext.setEnabled(currentPage < totalPages - 1);
                }
            }

            @Override
            public void onFailure(Call<ContractPageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- PHÂN TRANG ---
    private void setupPaginationListeners() {
        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) fetchContracts(currentKeyword, currentRole,currentPage - 1);
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) fetchContracts(currentKeyword,currentRole, currentPage + 1);
        });
    }

    private void setupSearchListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();

                currentKeyword = keyword.isEmpty() ? null : keyword;
                Log.d("SEARCH", "keyword = " + currentKeyword);
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    fetchContracts(currentKeyword, currentRole, 0);
                };
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupRoleFilterListener() {
        searchSpinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();

                if (selected.equals("TENANT")) {
                    currentRole = "TENANT";
                } else if (selected.equals("OWNER")) {
                    currentRole = "OWNER";
                } else {
                    currentRole = null; // Tất cả
                }

                fetchContracts(currentKeyword, currentRole, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentRole = null;
            }
        });
    }
}