package com.ptithcm.apt.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.resident.MemberRequest;
import com.ptithcm.apt.models.resident.ResidentListResponse;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberFragment extends Fragment {

    private Toolbar toolbar;
    private TextView tvApartmentInfo;
    private TextInputEditText edtName, edtCccd, edtPhone, edtEmail;
    private Button btnSelectDob, btnSubmit;

    private String roomNumber = "";
    private String selectedDob = ""; // Lưu ngày sinh chuẩn yyyy-MM-dd

    public AddMemberFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        if (getArguments() != null) {
            roomNumber = getArguments().getString("ROOM_NUMBER", "");
        }

        initViews(view);
        setupEvents();

        return view;
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar_add_member);
        tvApartmentInfo = view.findViewById(R.id.tv_add_member_apartment_info);
        edtName = view.findViewById(R.id.edt_add_member_name);
        edtCccd = view.findViewById(R.id.edt_add_member_cccd);
        edtPhone = view.findViewById(R.id.edt_add_member_phone);
        edtEmail = view.findViewById(R.id.edt_add_member_email);
        btnSelectDob = view.findViewById(R.id.btn_select_dob);
        btnSubmit = view.findViewById(R.id.btn_submit_add_member);

        if (!roomNumber.isEmpty()) {
            tvApartmentInfo.setText("Đang thêm thành viên vào phòng: " + roomNumber);
        } else {
            tvApartmentInfo.setText("LỖI: Không tìm thấy Số phòng!");
            btnSubmit.setEnabled(false);
        }

        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void setupEvents() {
        // Mở lịch chọn ngày sinh
        btnSelectDob.setOnClickListener(v -> showDatePicker());

        // Bấm nút XÁC NHẬN
        btnSubmit.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String cccd = edtCccd.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (name.isEmpty() || cccd.isEmpty() || email.isEmpty() || selectedDob.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin và chọn ngày sinh!", Toast.LENGTH_SHORT).show();
                return;
            }

            MemberRequest request = new MemberRequest(name, selectedDob, phone, cccd, email);
            callAddMemberApi(request);
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 20; // Mặc định mở ra lùi lại 20 năm
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            // Format về chuẩn yyyy-MM-dd cho Spring Boot
            selectedDob = String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            btnSelectDob.setText(selectedDob);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void callAddMemberApi(MemberRequest request) {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.addMemberToApartment(roomNumber, request).enqueue(new Callback<ResidentListResponse>() {
            @Override
            public void onResponse(Call<ResidentListResponse> call, Response<ResidentListResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm thành viên thành công!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Quay lại trang trước
                } else {
                    // Bắt lỗi từ Backend (Ví dụ: Trùng CCCD, trùng Email)
                    String errorMsg = "Lỗi khi thêm thành viên!";
                    try {
                        if (response.errorBody() != null) {
                            String errorStr = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorStr);
                            if (jsonObject.has("message")) errorMsg = jsonObject.getString("message");
                        }
                    } catch (Exception e) { e.printStackTrace(); }
                    Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResidentListResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}