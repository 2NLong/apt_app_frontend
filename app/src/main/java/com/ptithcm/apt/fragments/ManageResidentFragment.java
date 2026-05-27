package com.ptithcm.apt.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog; // Thêm import này cho AlertDialog
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.ResidentAdapter;

import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.models.resident.ResidentPageResponse;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageResidentFragment extends Fragment {

    private Toolbar toolbar;
    private RecyclerView rvResidents;
    private EditText edtSearch;
    private Button btnPrev, btnNext;
    private TextView tvPageInfo;
    private FloatingActionButton fabAddResident;

    private ResidentAdapter adapter;

    private int currentPage = 0;
    private int totalPages = 1;
    private String currentKeyword = null;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ManageResidentFragment() {
        // Required empty public constructor
    }

    public static ManageResidentFragment newInstance(String param1, String param2) {
        ManageResidentFragment fragment = new ManageResidentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_resident, container, false);

        initViews(view);
        setupRecyclerView();
        setupEvents();

        fetchResidents(currentKeyword, 0);

        return view;
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar_manage_resident);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        rvResidents = view.findViewById(R.id.rv_residents);
        edtSearch = view.findViewById(R.id.edt_search_resident);
        btnPrev = view.findViewById(R.id.btn_prev_page_res);
        btnNext = view.findViewById(R.id.btn_next_page_res);
        tvPageInfo = view.findViewById(R.id.tv_page_info_res);
        fabAddResident = view.findViewById(R.id.fab_add_resident);
    }

    private void setupRecyclerView() {
        rvResidents.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ResidentAdapter(new ArrayList<>(), resident -> {
            Bundle bundle = new Bundle();
            bundle.putLong("RESIDENT_ID", resident.getResidentId());

            Fragment updateFragment = new UpdateResidentFragment();
            updateFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, updateFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rvResidents.setAdapter(adapter);
    }

    private void setupEvents() {
        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) fetchResidents(currentKeyword, currentPage - 1);
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) fetchResidents(currentKeyword, currentPage + 1);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();
                currentKeyword = keyword.isEmpty() ? null : keyword;

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> fetchResidents(currentKeyword, 0);
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        fabAddResident.setOnClickListener(v -> showAddMemberDialog());
    }

    private void fetchResidents(String keyword, int page) {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);

        apiService.getActiveResidents(keyword, page, 10).enqueue(new Callback<ResidentPageResponse>() {
            @Override
            public void onResponse(Call<ResidentPageResponse> call, Response<ResidentPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResidentPageResponse pageResponse = response.body();

                    if (page == 0) {
                        adapter.setData(pageResponse.getContent());
                    } else {
                        adapter.addData(pageResponse.getContent());
                    }

                    totalPages = pageResponse.getTotalPages();
                    currentPage = pageResponse.getNumber();

                    tvPageInfo.setText((currentPage + 1) + " / " + Math.max(1, totalPages));
                    btnPrev.setEnabled(currentPage > 0);
                    btnNext.setEnabled(currentPage < totalPages - 1);
                }
            }

            @Override
            public void onFailure(Call<ResidentPageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddMemberDialog() {
        final EditText input = new EditText(getContext());
        input.setHint("Nhập Số phòng (Ví dụ: 101, 202)");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);

        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        input.setPadding(padding, padding, padding, padding);

        // 1. Tạo dialog nhưng không gọi .show() ngay
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Thêm Thành viên mới")
                .setMessage("Vui lòng nhập Số phòng mà cư dân này sẽ chuyển vào:")
                .setView(input)
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .setPositiveButton("Tiếp tục", null) // Set null để tước quyền tự đóng
                .create();

        dialog.show();

        // 2. Ép sự kiện OnClick thủ công cho nút Tiếp tục sau khi bảng đã hiện
        Button btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        btnPositive.setOnClickListener(v -> {
            String roomNumber = input.getText().toString().trim();

            if (roomNumber.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập Số phòng!", Toast.LENGTH_SHORT).show();
                return; // Lỗi rỗng -> Báo lỗi, bảng không bị đóng
            }

            // Đổi giao diện nút bấm để khóa người dùng spam click
            btnPositive.setText("Đang kiểm tra...");
            btnPositive.setEnabled(false);

            // 3. Gọi API kiểm tra phòng
            ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);
            apiService.searchApartments(roomNumber).enqueue(new Callback<List<Apartment>>() {
                @Override
                public void onResponse(Call<List<Apartment>> call, Response<List<Apartment>> response) {
                    btnPositive.setText("Tiếp tục");
                    btnPositive.setEnabled(true);

                    if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                        boolean isExactMatch = false;
                        for (Apartment apt : response.body()) {
                            if (apt.getRoomNumber().equals(roomNumber)) {
                                isExactMatch = true;
                                break;
                            }
                        }

                        if (isExactMatch) {
                            dialog.dismiss();
                            Bundle bundle = new Bundle();
                            bundle.putString("ROOM_NUMBER", roomNumber);
                            Fragment addFragment = new AddMemberFragment();
                            addFragment.setArguments(bundle);
                            requireActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.admin_fragment_container, addFragment)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getContext(), "Phòng " + roomNumber + " không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Phòng " + roomNumber + " không tồn tại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Apartment>> call, Throwable t) {
                    btnPositive.setText("Tiếp tục");
                    btnPositive.setEnabled(true);
                    Toast.makeText(getContext(), "Lỗi mạng, vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}