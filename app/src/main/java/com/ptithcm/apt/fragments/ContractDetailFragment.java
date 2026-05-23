package com.ptithcm.apt.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.contract.ContractResponse;
// Nhớ đảm bảo bạn đã import đúng đường dẫn model này từ lúc làm tính năng Danh sách cư dân
import com.ptithcm.apt.models.resident.ResidentListResponse;
import com.ptithcm.apt.network.api.ContractApiService;
import com.ptithcm.apt.network.api.ResidentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractDetailFragment extends Fragment {

    private TextView tvStatus, tvResidentName, tvPhone, tvCccd;
    private TextView tvRoom, tvRole, tvRentalPrice, tvDeposit, tvDates;

    // ĐÃ THÊM: Các biến cho phần Thông tin Chủ hộ
    private TextView tvOwnerTitle, tvOwnerName, tvOwnerPhone;
    private CardView cvOwnerInfo;

    private Long contractId = -1L;
    private Button btnMoveOut;
    private Long currentResidentId;
    private Long currentApartmentId;

    public ContractDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_contract_detail,container,false);
        if(getArguments() != null){
            contractId = getArguments().getLong("CONTRACT_ID",-1L);
        }

        initViews(view);

        view.findViewById(R.id.toolbar_contract_detail).setOnClickListener(v->{
            getParentFragmentManager().popBackStack();
        });

        btnMoveOut.setOnClickListener(v -> showMoveOutConfirmDialog());

        if (contractId != -1L) {
            fetchContractDetail(contractId);
        } else {
            Toast.makeText(getContext(), "Lỗi: Không lấy được ID Hợp đồng!", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void initViews(View view){
        tvStatus = view.findViewById(R.id.tv_detail_status);
        tvResidentName = view.findViewById(R.id.tv_detail_resident_name);
        tvPhone = view.findViewById(R.id.tv_detail_phone);
        tvCccd = view.findViewById(R.id.tv_detail_cccd);

        tvRoom = view.findViewById(R.id.tv_detail_room);
        tvRole = view.findViewById(R.id.tv_detail_role);
        tvRentalPrice = view.findViewById(R.id.tv_detail_rental_price);
        tvDeposit = view.findViewById(R.id.tv_detail_deposit);
        tvDates = view.findViewById(R.id.tv_detail_dates);
        btnMoveOut = view.findViewById(R.id.btn_move_out);

        tvOwnerTitle = view.findViewById(R.id.tv_title_owner_info);
        cvOwnerInfo = view.findViewById(R.id.cv_owner_info);
        tvOwnerName = view.findViewById(R.id.tv_detail_owner_name);
        tvOwnerPhone = view.findViewById(R.id.tv_detail_owner_phone);
    }

    private void fetchContractDetail(Long id){
        ContractApiService apiService = RetrofitClient.getInstance().createService(ContractApiService.class);

        apiService.getContractDetail(id).enqueue(new Callback<ContractResponse>() {
            @Override
            public void onResponse(Call<ContractResponse> call, Response<ContractResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    displayData(response.body());
                }else{
                    Toast.makeText(getContext(),"Không thể tải chi tiết hợp đồng",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ContractResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayData(ContractResponse data) {
        currentResidentId = data.getResidentId();
        currentApartmentId = data.getApartmentId();
        DecimalFormat formatter = new DecimalFormat("#,###");

        tvResidentName.setText(data.getResidentName());
        tvPhone.setText(data.getPhone() != null ? data.getPhone() : "Chưa cập nhật");
        tvCccd.setText(data.getCitizenIdentity() != null ? data.getCitizenIdentity() : "Chưa cập nhật");

        tvRoom.setText("Phòng " + data.getRoomNumber());

        if ("OWNER".equals(data.getRole())) {
            tvRole.setText("CHỦ HỘ");
            tvOwnerTitle.setVisibility(View.GONE);
            cvOwnerInfo.setVisibility(View.GONE);
        } else {
            tvRole.setText("NGƯỜI THUÊ");
            tvOwnerTitle.setVisibility(View.VISIBLE);
            cvOwnerInfo.setVisibility(View.VISIBLE);
            fetchOwnerInfo(currentApartmentId);
        }

        tvRentalPrice.setText(data.getRentalPrice() != null ? formatter.format(data.getRentalPrice()) : "0");
        tvDeposit.setText(data.getDepositAmount() != null ? formatter.format(data.getDepositAmount()) : "0");

        String start = data.getContractStart() != null ? data.getContractStart() : "...";
        String end = data.getContractEnd() != null ? data.getContractEnd() : "...";
        tvDates.setText(start + " đến " + end);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(new Date());

        boolean isStillValid = false;

        if (data.getContractEnd() != null) {
            if (data.getContractEnd().compareTo(todayStr) >= 0) {
                isStillValid = true;
            }
        } else if (data.getContractStart() != null) {
            isStillValid = true;
        }

        if (isStillValid) {
            tvStatus.setText("ĐANG HIỆU LỰC");
            tvStatus.setBackgroundResource(R.drawable.bg_button);
            tvStatus.setTextColor(Color.parseColor("#A63C4F"));
        } else {
            tvStatus.setText("ĐÃ KẾT THÚC / HỦY");
            tvStatus.setBackgroundResource(R.drawable.bg_button);
            tvStatus.setTextColor(Color.parseColor("#666666"));
            btnMoveOut.setVisibility(View.GONE);
        }
    }

    private void fetchOwnerInfo(Long aptId) {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);

        apiService.getResidentsInApartment(aptId).enqueue(new Callback<List<ResidentListResponse>>() {
            @Override
            public void onResponse(Call<List<ResidentListResponse>> call, Response<List<ResidentListResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (ResidentListResponse resident : response.body()) {
                        if ("OWNER".equals(resident.getRole())) {
                            tvOwnerName.setText(resident.getFullName());
                            tvOwnerPhone.setText(resident.getPhone() != null ? resident.getPhone() : "Chưa cập nhật");
                            return;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ResidentListResponse>> call, Throwable t) {
            }
        });
    }

    private void showMoveOutConfirmDialog() {
        if (currentResidentId == null || currentApartmentId == null) return;

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("CẢNH BÁO THANH LÝ")
                .setMessage("Bạn đang thực hiện thanh lý hợp đồng cho Chủ hộ. Nếu tiếp tục, TOÀN BỘ thành viên trong phòng này sẽ được hệ thống cho dọn đi và trả phòng về trạng thái trống.\n\nBạn có chắc chắn muốn thực hiện?")
                .setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Xác nhận", (dialog, which) -> executeMoveOutApi())
                .show();
    }

    private void executeMoveOutApi() {
        ResidentApiService apiService = RetrofitClient.getInstance().createService(ResidentApiService.class);

        apiService.moveOutResident(currentResidentId, currentApartmentId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thanh lý hợp đồng thành công!", Toast.LENGTH_LONG).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    String errorMessage = "Lỗi hệ thống khi thanh lý hợp đồng!";
                    try {
                        if (response.errorBody() != null) {
                            String errorStr = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorStr);
                            if (jsonObject.has("message")) {
                                errorMessage = jsonObject.getString("message");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    showErrorDialog(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorDialog(String message) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Không thể thanh lý")
                .setMessage(message)
                .setPositiveButton("Đã hiểu", (dialog, which) -> dialog.dismiss())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}