package com.ptithcm.apt.fragments.rentinvoice;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceDetailResponse;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModelFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminRentInvoiceDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminRentInvoiceDetailFragment extends Fragment {
    private static final String ARG_RENT_ID = "rent_id";
    private Long rentId;
    private AdminBillViewModel viewModel;

    // Views
    private TextView tvStatus, tvTotal, tvBillingTime, tvApartment, tvInfo, tvOwner, tvTenant;
    private TextView tvRentAmount, tvCreatedAt, tvCreatedBy, tvDueDate, tvPaidAt;
    private ImageView btnBack;
    private Button btnApprove;

    public static AdminRentInvoiceDetailFragment newInstance(Long id) {
        AdminRentInvoiceDetailFragment fragment = new AdminRentInvoiceDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RENT_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) rentId = getArguments().getLong(ARG_RENT_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_rent_invoice_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupViewModel();
        setupListeners();
        observeViewModel();

        // Gọi API lấy chi tiết
        viewModel.fetchRentDetail(rentId);
    }

    private void initViews(View v) {
        btnBack = v.findViewById(R.id.btnBack);
        tvStatus = v.findViewById(R.id.tvDetailStatus);
        tvTotal = v.findViewById(R.id.tvDetailTotal);
        tvBillingTime = v.findViewById(R.id.tvDetailBillingTime);
        tvApartment = v.findViewById(R.id.tvDetailApartment);
        tvInfo = v.findViewById(R.id.tvDetailInfo);
        tvOwner = v.findViewById(R.id.tvDetailOwner);
        tvTenant = v.findViewById(R.id.tvDetailTenant);
        tvRentAmount = v.findViewById(R.id.tvDetailRentAmount);
        tvCreatedAt = v.findViewById(R.id.tvDetailCreatedAt);
        tvCreatedBy = v.findViewById(R.id.tvDetailCreatedBy);
        tvDueDate = v.findViewById(R.id.tvDetailDueDate);
        tvPaidAt = v.findViewById(R.id.tvDetailPaidAt);
        btnApprove = v.findViewById(R.id.btnApproveRent);
    }

    private void setupViewModel() {
        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());

        btnApprove.setOnClickListener(v -> {
            // Bước 1: Hiện Dialog xác nhận
            DialogUtils.showConfirmDialog(
                    requireContext(),
                    "Xác nhận duyệt phí",
                    "Xác nhận căn hộ " + tvApartment.getText() + " đã đóng tiền thuê nhà?",
                    () -> {
                        // Bước 2: Gọi ViewModel thực hiện API
                        viewModel.approveRentInvoice(rentId);
                    }
            );
        });
    }

    private void observeViewModel() {
        // Lưu ý: Bạn cần thêm LiveData rentDetail vào ViewModel tương tự như billDetail
        viewModel.rentDetail.observe(getViewLifecycleOwner(), detail -> {
            if (detail != null) {
                bindData(detail);
            }
        });

        viewModel.updateRentSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null && isSuccess) {
                ToastUtils.showSuccessToast(requireContext(), "Duyệt hóa đơn thành công!");
                // Duyệt xong quay về màn hình danh sách
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
            }
        });

        // Theo dõi lỗi từ hệ thống (Bao gồm lỗi 500 bạn vừa gặp)
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                ToastUtils.showErrorToast(requireContext(), errorMsg);
            }
        });
    }

    private void bindData(RentInvoiceDetailResponse data) {
        String status = data.getStatus();

        if ("PAID".equals(status)) {
            tvStatus.setText("ĐÃ THANH TOÁN");
            tvStatus.setTextColor(Color.parseColor("#4CAF50"));
            btnApprove.setVisibility(View.GONE);
        } else if ("LATE".equals(status)) {
            tvStatus.setText("QUÁ HẠN");
            tvStatus.setTextColor(Color.parseColor("#F44336"));
            btnApprove.setVisibility(View.VISIBLE);
        } else {
            tvStatus.setText("CHƯA THANH TOÁN");
            tvStatus.setTextColor(Color.parseColor("#FF9800"));
            btnApprove.setVisibility(View.VISIBLE);
        }

        DecimalFormat formatter = new DecimalFormat("#,###đ");
        tvTotal.setText(formatter.format(data.getRentAmount()));
        tvRentAmount.setText(formatter.format(data.getRentAmount()));

        tvBillingTime.setText("Tiền thuê tháng " + String.format("%02d", data.getBillingMonth()) + "/" + data.getBillingYear());

        // 2. Thông tin căn hộ
        tvApartment.setText(data.getApartmentName());
        tvInfo.setText("Tầng " + data.getApartmentFloor() + " - " + data.getApartmentArea() + " m²");
        tvOwner.setText(data.getOwnerName());
        tvTenant.setText(data.getTenantName());

        // 3. Thời gian (Helper để parse chuỗi ISO sang dd/MM/yyyy)
        tvCreatedAt.setText(formatIsoDate(data.getCreatedAt()));
        tvCreatedBy.setText(data.getCreatedBy());
        tvDueDate.setText(formatIsoDate(data.getDueDate()));
    }

    private String formatIsoDate(String isoDate) {
        if (isoDate == null || isoDate.length() < 10) return "--";
        try {
            // Cắt lấy phần yyyy-MM-dd
            String datePart = isoDate.substring(0, 10);
            SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = apiFormat.parse(datePart);
            return displayFormat.format(date);
        } catch (Exception e) {
            return isoDate;
        }
    }


}