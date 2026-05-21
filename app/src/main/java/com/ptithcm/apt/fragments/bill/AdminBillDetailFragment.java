package com.ptithcm.apt.fragments.bill;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.R;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModelFactory;

import java.util.Locale;

public class AdminBillDetailFragment extends Fragment {

    private static final String ARG_BILL_ID = "bill_id";
    private long mBillId;
    private AdminBillViewModel viewModel;

    // UI Elements
    private ImageView btnBack;
    private TextView tvStatus, tvTotal, tvMonth;
    private TextView tvRoom, tvFloor, tvArea;
    private TextView tvElectric, tvWater, tvManagement, tvSanitation;
    private TextView tvCreatedAt, tvCreatedBy, tvDueDate, tvPaidAt, tvApprovedBy;
    private Button btnApprove;

    public AdminBillDetailFragment() {}

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_bill_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);

        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);

        setupClickListeners();

        observeViewModel();

        viewModel.fetchBillDetail(mBillId);
    }

    private void observeViewModel() {
        viewModel.billDetail.observe(getViewLifecycleOwner(), detail -> {
            if (detail != null) {
                if (detail.getStatus() == BillStatus.PAID) {
                    tvStatus.setText("ĐÃ THANH TOÁN");
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"));
                    btnApprove.setVisibility(View.GONE);
                } else if (detail.getStatus() == BillStatus.LATE) {
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

                tvTotal.setText(formatCurrency(detail.getTotalAmount()));
                tvMonth.setText("Hóa đơn tháng " + detail.getBillingMonth() + "/" + detail.getBillingYear());
                tvRoom.setText(detail.getApartmentName());
                tvFloor.setText("Tầng " + detail.getApartmentFloor());
                tvArea.setText(detail.getApartmentArea() + " m²");
                tvElectric.setText(formatCurrency(detail.getElectricityFee()));
                tvWater.setText(formatCurrency(detail.getWaterFee()));
                tvManagement.setText(formatCurrency(detail.getManagementFee()));
                tvSanitation.setText(formatCurrency(detail.getSanitationFee()));
                tvCreatedAt.setText(formatDate(detail.getCreatedAt()));
                tvDueDate.setText(formatDate(detail.getDueDate()));
                tvPaidAt.setText(formatDate(detail.getPaidAt()));
                tvCreatedBy.setText(detail.getCreatedBy() != null ? detail.getCreatedBy() : "--");
                tvApprovedBy.setText(detail.getConfirmBy() != null ? detail.getConfirmBy() : "--");
            }
        });

        viewModel.updateStatusSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null && isSuccess) {
                ToastUtils.showSuccessToast(requireContext(), "Duyệt hóa đơn thành công!");

                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });


        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                ToastUtils.showErrorToast(requireContext(), errorMsg);
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });

        btnApprove.setOnClickListener(v -> {
            DialogUtils.showConfirmDialog(
                    requireContext(),
                    "Xác nhận thanh toán",
                    "Bạn có chắc muốn duyệt hóa đơn cho căn hộ " + tvRoom.getText() + " không?",
                    this::approveBill
            );
        });
    }

    private void approveBill() {
        viewModel.approveBill(mBillId);
    }

    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "--";
        try {
            String datePart = isoDate.split("T")[0];
            String[] parts = datePart.split("-");
            if (parts.length == 3) return parts[2] + "/" + parts[1] + "/" + parts[0];
            return datePart;
        } catch (Exception e) {
            return "--";
        }
    }

    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) return "0đ";
        return String.format(Locale.getDefault(), "%,.0fđ", amount.doubleValue());
    }

    private void initViews(View v) {
        btnBack = v.findViewById(R.id.btnBack);
        tvStatus = v.findViewById(R.id.tvDetailStatus);
        tvTotal = v.findViewById(R.id.tvDetailTotal);
        tvMonth = v.findViewById(R.id.tvDetailBillingMonth);
        tvRoom = v.findViewById(R.id.tvDetailRoom);
        tvFloor = v.findViewById(R.id.tvDetailFloor);
        tvArea = v.findViewById(R.id.tvDetailArea);
        tvElectric = v.findViewById(R.id.tvDetailElectric);
        tvWater = v.findViewById(R.id.tvDetailWater);
        tvManagement = v.findViewById(R.id.tvDetailManagement);
        tvSanitation = v.findViewById(R.id.tvDetailSanitation);
        tvCreatedAt = v.findViewById(R.id.tvDetailCreatedAt);
        tvCreatedBy = v.findViewById(R.id.tvDetailCreatedBy);
        tvDueDate = v.findViewById(R.id.tvDetailDueDate);
        tvPaidAt = v.findViewById(R.id.tvDetailPaidAt);
        tvApprovedBy = v.findViewById(R.id.tvDetailApprovedBy);
        btnApprove = v.findViewById(R.id.btnPay);
    }
}