package com.ptithcm.apt.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.resident.ResidentDetailResponse;
import com.ptithcm.apt.models.resident.UpdateResidentRequest;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateResidentFragment extends Fragment {

    private Toolbar toolbar;
    private TextInputEditText edtName, edtCccd, edtPhone, edtEmail;
    private Button btnSave, btnMoveOut;

    private Long residentId = -1L;
    private ResidentDetailResponse currentResidentData;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UpdateResidentFragment() {
        // Required empty public constructor
    }

    public static UpdateResidentFragment newInstance(String param1, String param2) {
        UpdateResidentFragment fragment = new UpdateResidentFragment();
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
            residentId = getArguments().getLong("RESIDENT_ID", -1L);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_resident, container, false);

        initViews(view);
        setupEvents();

        if (residentId != -1L) {
            loadDetail();
        } else {
            showErrorToast("Lỗi: Không nhận được ID Cư dân");
        }

        return view;
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar_update_resident);
        edtName = view.findViewById(R.id.edt_up_res_name);
        edtCccd = view.findViewById(R.id.edt_up_res_cccd);
        edtPhone = view.findViewById(R.id.edt_up_res_phone);
        edtEmail = view.findViewById(R.id.edt_up_res_email);
        btnSave = view.findViewById(R.id.btn_up_res_save);
        btnMoveOut = view.findViewById(R.id.btn_up_res_move_out);
    }

    private void setupEvents() {
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        btnSave.setOnClickListener(v -> updateInfo());
        btnMoveOut.setOnClickListener(v -> handleMoveOutClick());
    }

    private void loadDetail() {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.getResidentDetail(residentId).enqueue(new Callback<ResidentDetailResponse>() {
            @Override
            public void onResponse(Call<ResidentDetailResponse> call, Response<ResidentDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentResidentData = response.body();
                    edtName.setText(currentResidentData.getFullName());
                    edtCccd.setText(currentResidentData.getCitizenIdentity());
                    edtPhone.setText(currentResidentData.getPhone());
                    edtEmail.setText(currentResidentData.getEmail());
                }
            }
            @Override
            public void onFailure(Call<ResidentDetailResponse> call, Throwable t) {
                showErrorToast("Lỗi kết nối tải dữ liệu!");
            }
        });
    }

    private void handleMoveOutClick() {
        if (currentResidentData == null || currentResidentData.getResidencies() == null || currentResidentData.getResidencies().isEmpty()) {
            showErrorToast("Cư dân này hiện không lưu trú tại phòng nào!");
            return;
        }

        List<ResidentDetailResponse.ResidencyInfo> residencies = currentResidentData.getResidencies();

        // Nếu chỉ ở 1 phòng, hỏi luôn
        if (residencies.size() == 1) {
            showMoveOutConfirm(residencies.get(0));
        }
        // Nếu ở nhiều phòng, hiển thị danh sách để chọn
        else {
            String[] roomNames = new String[residencies.size()];
            for (int i = 0; i < residencies.size(); i++) {
                String roleVN = "Khác";
                if ("OWNER".equals(residencies.get(i).getRole())) roleVN = "Chủ hộ";
                else if ("TENANT".equals(residencies.get(i).getRole())) roleVN = "Người thuê";
                else if ("MEMBER".equals(residencies.get(i).getRole())) roleVN = "Thành viên";

                roomNames[i] = "Phòng " + residencies.get(i).getRoomNumber() + " (" + roleVN + ")";
            }

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Chọn phòng muốn dọn đi")
                    .setItems(roomNames, (dialog, which) -> {
                        showMoveOutConfirm(residencies.get(which));
                    })
                    .show();
        }
    }

    private void showMoveOutConfirm(ResidentDetailResponse.ResidencyInfo roomInfo) {
        String message = (roomInfo.getIsHead() != null && roomInfo.getIsHead())
                ? "CẢNH BÁO: Đây là Chủ hộ/Đại diện! Nếu chuyển đi, TẤT CẢ thành viên trong phòng " + roomInfo.getRoomNumber() + " sẽ phải dọn đi theo. Bạn chắc chứ?"
                : "Xác nhận cho cư dân " + currentResidentData.getFullName() + " chuyển ra khỏi phòng " + roomInfo.getRoomNumber() + "?";

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận chuyển đi")
                .setMessage(message)
                .setNegativeButton("Hủy", (d, w) -> d.dismiss())
                .setPositiveButton("Thực hiện", (d, w) -> callMoveOutApi(roomInfo.getApartmentId()))
                .show();
    }

    private void callMoveOutApi(Long apartmentId) {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.moveOutResident(residentId, apartmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    showSuccessToast("Đã xử lý chuyển đi thành công!");
                    getParentFragmentManager().popBackStack();
                } else {
                    handleError(response);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorToast("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void updateInfo() {
        String name = edtName.getText().toString().trim();
        String cccd = edtCccd.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || cccd.isEmpty() || email.isEmpty()) {
            showErrorToast("Vui lòng nhập đủ các thông tin bắt buộc!");
            return;
        }

        String dob = currentResidentData != null ? currentResidentData.getDob() : null;

        UpdateResidentRequest request = new UpdateResidentRequest(name, dob, phone, cccd, email);

        btnSave.setEnabled(false);
        btnSave.setText("Đang lưu...");

        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.updateResident(residentId, request).enqueue(new Callback<ResidentDetailResponse>() {
            @Override
            public void onResponse(Call<ResidentDetailResponse> call, Response<ResidentDetailResponse> response) {
                btnSave.setEnabled(true);
                btnSave.setText("LƯU THAY ĐỔI");

                if (response.isSuccessful()) {
                    showSuccessToast("Cập nhật thông tin thành công!");
                    getParentFragmentManager().popBackStack();
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<ResidentDetailResponse> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("LƯU THAY ĐỔI");
                showErrorToast("Lỗi mạng: " + t.getMessage());
            }
        });
    }

    private void handleError(Response<?> response) {
        try {
            JSONObject json = new JSONObject(response.errorBody().string());
            String message = json.optString("message", "Có lỗi xảy ra khi xử lý!");
            showErrorToast(message);
        } catch (Exception e) {
            showErrorToast("Có lỗi xảy ra khi xử lý!");
        }
    }

    private void showSuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast_success, null);

        TextView text = layout.findViewById(R.id.tv_toast_message_success);
        text.setText(message);

        Toast toast = new Toast(requireActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast_error, null);

        TextView text = layout.findViewById(R.id.tv_toast_message_error);
        text.setText(message);

        Toast toast = new Toast(requireActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}