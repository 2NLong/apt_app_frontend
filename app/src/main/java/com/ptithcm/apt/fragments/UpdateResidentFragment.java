package com.ptithcm.apt.fragments;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.resident.ResidentDetailResponse;
import com.ptithcm.apt.models.resident.UpdateResidentRequest;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateResidentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateResidentFragment extends Fragment {

    private Toolbar toolbar;
    private TextInputEditText edtName, edtCccd, edtPhone, edtEmail;
    private Button btnSave, btnMoveOut;

    private Long residentId = -1L;
    private ResidentDetailResponse currentResidentData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateResidentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateResidentFragment.
     */
    // TODO: Rename and change types and number of parameters
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_resident, container, false);

        // 2. Lấy ID từ màn hình danh sách truyền sang
        if (getArguments() != null) {
            residentId = getArguments().getLong("RESIDENT_ID", -1L);
        }

        // 3. GỌI CÁC HÀM KHỞI TẠO VÀ TẢI DỮ LIỆU TẠI ĐÂY
        initViews(view);
        setupEvents();

        if (residentId != -1L) {
            loadDetail();
        } else {
            Toast.makeText(getContext(), "Lỗi: Không nhận được ID Cư dân", Toast.LENGTH_SHORT).show();
        }

        // 4. Cuối cùng mới return
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

        btnMoveOut.setOnClickListener(v -> showMoveOutConfirm());
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
                Toast.makeText(getContext(), "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMoveOutConfirm() {
        if (currentResidentData == null) return;

        // Cảnh báo dựa trên vai trò Head hay Member
        String message = currentResidentData.getIsHead()
                ? "CẢNH BÁO: Đây là Chủ hộ! Nếu thực hiện chuyển đi, TẤT CẢ thành viên trong phòng " + currentResidentData.getRoomNumber() + " sẽ bị hệ thống cho dọn đi theo. Bạn chắc chứ?"
                : "Xác nhận cho cư dân " + currentResidentData.getFullName() + " chuyển ra khỏi phòng " + currentResidentData.getRoomNumber() + "?";

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận chuyển đi")
                .setMessage(message)
                .setNegativeButton("Hủy", (d, w) -> d.dismiss())
                .setPositiveButton("Thực hiện", (d, w) -> callMoveOutApi())
                .show();
    }

    private void callMoveOutApi() {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.moveOutResident(residentId, currentResidentData.getApartmentId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Xử lý thành công", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    handleError(response);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) { /* Toast lỗi kết nối */ }
        });
    }

    private void updateInfo() {
        String name = edtName.getText().toString().trim();
        String cccd = edtCccd.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || cccd.isEmpty() || email.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ các thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        String dob = currentResidentData != null ? currentResidentData.getDob() : null;

        UpdateResidentRequest request = new UpdateResidentRequest(name, dob, phone, cccd, email);

        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);
        apiService.updateResident(residentId, request).enqueue(new Callback<ResidentDetailResponse>() {
            @Override
            public void onResponse(Call<ResidentDetailResponse> call, Response<ResidentDetailResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack(); // Lưu xong thì lùi về danh sách
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<ResidentDetailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleError(Response<?> response) {
        try {
            JSONObject json = new JSONObject(response.errorBody().string());
            Toast.makeText(getContext(), json.optString("message", "Lỗi"), Toast.LENGTH_LONG).show();
        } catch (Exception e) { e.printStackTrace(); }
    }
}