package com.ptithcm.apt.fragments.rentinvoice;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ptithcm.apt.R;
import com.ptithcm.apt.models.rentinvoice.response.UserRentInvoiceDetailResponse;
import com.ptithcm.apt.viewmodel.bill.UserBillViewModel;
import com.ptithcm.apt.viewmodel.bill.UserBillViewModelFactory;
import com.ptithcm.apt.utils.ToastUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserRentInvoiceDetailFragment extends Fragment {

    private static final String ARG_INVOICE_ID = "invoice_id";
    private int invoiceId;

    private TextView tvStatus, tvAmount, tvBillingMonth, tvRoom, tvPrice, tvCreatedAt, tvDueDate, tvPaidAt;
    private View layoutPaidAt, layoutNote;
    private ImageView btnBack;

    private UserBillViewModel viewModel;

    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public UserRentInvoiceDetailFragment() {}

    public static UserRentInvoiceDetailFragment newInstance(int invoiceId) {
        UserRentInvoiceDetailFragment fragment = new UserRentInvoiceDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INVOICE_ID, invoiceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoiceId = getArguments().getInt(ARG_INVOICE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_rent_invoice_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupViewModel();
    }

    private void initViews(View v) {
        tvStatus = v.findViewById(R.id.tvRentDetailStatus);
        tvAmount = v.findViewById(R.id.tvRentDetailAmount);
        tvBillingMonth = v.findViewById(R.id.tvRentDetailBillingMonth);
        tvRoom = v.findViewById(R.id.tvRentDetailRoom);
        tvPrice = v.findViewById(R.id.tvRentDetailPrice);
        tvCreatedAt = v.findViewById(R.id.tvRentDetailCreatedAt);
        tvDueDate = v.findViewById(R.id.tvRentDetailDueDate);
        tvPaidAt = v.findViewById(R.id.tvRentDetailPaidAt);
        layoutPaidAt = v.findViewById(R.id.layoutPaidAt);
        layoutNote = v.findViewById(R.id.layoutRentPaymentNote);
        btnBack = v.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view1 -> {
            if (getParentFragmentManager() != null) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void setupViewModel() {
        UserBillViewModelFactory factory = new UserBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(UserBillViewModel.class);

        // Lắng nghe dữ liệu chi tiết
        viewModel.rentInvoiceDetail.observe(getViewLifecycleOwner(), this::displayData);

        // Lắng nghe lỗi
        viewModel.error.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) ToastUtils.showErrorToast(requireContext(), msg);
        });

        // Gọi API lấy chi tiết
        viewModel.fetchRentInvoiceDetail(invoiceId);
    }

    public void displayData(UserRentInvoiceDetailResponse detail) {
        if (detail == null) return;

        String formattedAmount = formatMoney(detail.getRentAmount());
        tvAmount.setText(formattedAmount);
        tvPrice.setText(formattedAmount);
        tvRoom.setText(detail.getApartmentName());
        tvBillingMonth.setText("Tiền thuê tháng " + detail.getBillingMonth() + "/" + detail.getBillingYear());

        // Logic xử lý trạng thái hiển thị tiếng Việt và màu sắc
        String status = detail.getStatus();

        if ("PAID".equals(status)) {
            tvStatus.setText("ĐÃ THANH TOÁN");
            tvStatus.setTextColor(Color.parseColor("#43A047")); // Màu xanh lá

            layoutNote.setVisibility(View.GONE);
            layoutPaidAt.setVisibility(View.VISIBLE);
            tvPaidAt.setText(formatDate(detail.getPaidAt()));

        } else if ("LATE".equals(status)) {
            // Trạng thái: QUÁ HẠN
            tvStatus.setText("QUÁ HẠN");
            tvStatus.setTextColor(Color.parseColor("#E53935"));

            layoutNote.setVisibility(View.VISIBLE);
            layoutPaidAt.setVisibility(View.GONE);

        } else {
            tvStatus.setText("CHƯA THANH TOÁN");
            tvStatus.setTextColor(Color.parseColor("#FF9800"));

            layoutNote.setVisibility(View.VISIBLE);
            layoutPaidAt.setVisibility(View.GONE);
        }

        tvCreatedAt.setText(formatDate(detail.getCreatedAt()));
        tvDueDate.setText(formatDate(detail.getDueDate()));
    }

    private String formatMoney(double amount) {
        return new DecimalFormat("#,###đ").format(amount);
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return "--";
        try {
            if (dateStr.contains(".")) {
                dateStr = dateStr.split("\\.")[0];
            }
            Date date = apiFormat.parse(dateStr);
            return displayFormat.format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }
}