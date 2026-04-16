package com.ptithcm.apt.fragments.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.R;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModelFactory;

public class AdminBillDetailFragment extends Fragment {

    private static final String ARG_BILL_ID = "bill_id";
    private long mBillId;

    // UI Elements
    private ImageView btnBack;
    private TextView tvStatus, tvTotal, tvMonth;
    private TextView tvRoom, tvFloor, tvArea;
    private TextView tvElectric, tvWater, tvManagement, tvSanitation;
    private TextView tvCreatedAt, tvCreatedBy, tvDueDate, tvPaidAt, tvApprovedBy;
    private Button btnApprove;

    public AdminBillDetailFragment() {
        // Required empty public constructor
    }

    public static AdminBillDetailFragment newInstance(long billId) {
        AdminBillDetailFragment fragment = new AdminBillDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_BILL_ID, billId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBillId = getArguments().getLong(ARG_BILL_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_bill_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupClickListeners();

        // 1. Khởi tạo ViewModel (sử dụng Factory hiện tại của bạn)
        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        AdminBillViewModel viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);

        // 2. Đăng ký nhận dữ liệu (Mapping dữ liệu vào UI)
        viewModel.billDetail.observe(getViewLifecycleOwner(), detail -> {
            if (detail != null) {
                if (detail.getStatus() == com.ptithcm.apt.enums.BillStatus.PAID) {
                    tvStatus.setText("ĐÃ THANH TOÁN");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                    btnApprove.setVisibility(View.GONE);
                } else if (detail.getStatus() == com.ptithcm.apt.enums.BillStatus.LATE) {
                    tvStatus.setText("QUÁ HẠN");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#F44336"));
                    btnApprove.setVisibility(View.VISIBLE);
                    btnApprove.setText("Duyệt (Quá hạn)");
                } else {
                    tvStatus.setText("CHƯA THANH TOÁN");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#FF9800"));
                    btnApprove.setVisibility(View.VISIBLE);
                    btnApprove.setText("Duyệt");
                }

                // 2. Định dạng tiền tệ
                tvTotal.setText(formatCurrency(detail.getTotalAmount()));
                tvMonth.setText("Hóa đơn tháng " + detail.getBillingMonth() + "/" + detail.getBillingYear());

                // 3. Thông tin căn hộ
                tvRoom.setText(detail.getApartmentName());
                tvFloor.setText("Tầng " + detail.getApartmentFloor());
                tvArea.setText(detail.getApartmentArea() + " m²");

                // 4. Chi tiết phí
                tvElectric.setText(formatCurrency(detail.getElectricityFee()));
                tvWater.setText(formatCurrency(detail.getWaterFee()));
                tvManagement.setText(formatCurrency(detail.getManagementFee()));
                tvSanitation.setText(formatCurrency(detail.getSanitationFee()));

                // 5. Định dạng ngày tháng dd/MM/yyyy
                tvCreatedAt.setText(formatDate(detail.getCreatedAt()));
                tvDueDate.setText(formatDate(detail.getDueDate()));
                tvPaidAt.setText(formatDate(detail.getPaidAt()));

                tvCreatedBy.setText(detail.getCreatedBy() != null ? detail.getCreatedBy() : "--");
                tvApprovedBy.setText(detail.getConfirmBy() != null ? detail.getConfirmBy() : "--");
            }
        });

        // 3. Đăng ký nhận lỗi nếu có
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

        // 4. Kích hoạt gọi API
        viewModel.fetchBillDetail(mBillId); // mBillId lấy từ getArguments()


    }

    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "--";
        try {
            // Chuỗi API có dạng: 2026-04-13T09:54:21...
            // Tách lấy phần yyyy-MM-dd
            String datePart = isoDate.split("T")[0];
            String[] parts = datePart.split("-");

            if (parts.length == 3) {
                // Sắp xếp lại thành dd/MM/yyyy
                return parts[2] + "/" + parts[1] + "/" + parts[0];
            }
            return datePart;
        } catch (Exception e) {
            return "--";
        }
    }
    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) return "0đ";
        return String.format("%,.0fđ", amount.doubleValue());
    }
    private void initViews(View v) {
        btnBack = v.findViewById(R.id.btnBack);

        // Header
        tvStatus = v.findViewById(R.id.tvDetailStatus);
        tvTotal = v.findViewById(R.id.tvDetailTotal);
        tvMonth = v.findViewById(R.id.tvDetailBillingMonth);

        // Apartment Info
        tvRoom = v.findViewById(R.id.tvDetailRoom);
        tvFloor = v.findViewById(R.id.tvDetailFloor);
        tvArea = v.findViewById(R.id.tvDetailArea);

        // Fees Detail
        tvElectric = v.findViewById(R.id.tvDetailElectric);
        tvWater = v.findViewById(R.id.tvDetailWater);
        tvManagement = v.findViewById(R.id.tvDetailManagement);
        tvSanitation = v.findViewById(R.id.tvDetailSanitation);

        // Timeline & Admin Info
        tvCreatedAt = v.findViewById(R.id.tvDetailCreatedAt);
        tvCreatedBy = v.findViewById(R.id.tvDetailCreatedBy);
        tvDueDate = v.findViewById(R.id.tvDetailDueDate);
        tvPaidAt = v.findViewById(R.id.tvDetailPaidAt);
        tvApprovedBy = v.findViewById(R.id.tvDetailApprovedBy);

        // Action Button
        btnApprove = v.findViewById(R.id.btnPay);
    }

    private void setupClickListeners() {
        // Sự kiện nút quay lại
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        // Sự kiện nút Duyệt (Sử dụng ID mBillId để gọi API duyệt)
        btnApprove.setOnClickListener(v -> {
            approveBill();
        });
    }


    private void approveBill() {
        // TODO: Gọi API Duyệt hóa đơn
        Toast.makeText(getContext(), "Đang tiến hành duyệt hóa đơn ID: " + mBillId, Toast.LENGTH_SHORT).show();
    }
}