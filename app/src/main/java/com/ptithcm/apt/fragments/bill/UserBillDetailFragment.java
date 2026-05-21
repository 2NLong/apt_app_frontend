package com.ptithcm.apt.fragments.bill;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ptithcm.apt.databinding.FragmentUserBillDetailBinding;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.models.bill.response.UserBillDetailResponse;
import com.ptithcm.apt.network.api.UserBillApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.UserBillRepository;
import com.ptithcm.apt.utils.ToastUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UserBillDetailFragment extends Fragment {

    private FragmentUserBillDetailBinding binding;
    private UserBillRepository repository;
    private Long billId;

    private final SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###đ");

    public UserBillDetailFragment() {}

    public static UserBillDetailFragment newInstance(Long billId) {
        UserBillDetailFragment fragment = new UserBillDetailFragment();
        Bundle args = new Bundle();
        args.putLong("BILL_ID", billId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            billId = getArguments().getLong("BILL_ID");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBillDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserBillApiService apiService = RetrofitClient.getInstance().createService(UserBillApiService.class);
        repository = new UserBillRepository(apiService);

        binding.btnBack.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        loadBillDetail();
    }

    private void loadBillDetail() {
        androidx.lifecycle.MutableLiveData<UserBillDetailResponse> data = new androidx.lifecycle.MutableLiveData<>();
        androidx.lifecycle.MutableLiveData<String> error = new androidx.lifecycle.MutableLiveData<>();

        repository.getBillDetail(billId, data, error);

        data.observe(getViewLifecycleOwner(), this::updateUI);
        error.observe(getViewLifecycleOwner(), msg -> ToastUtils.showErrorToast(requireContext(), msg));
    }

    private void updateUI(UserBillDetailResponse bill) {
        binding.tvDetailTotal.setText(moneyFormat.format(bill.getTotalAmount()));
        binding.tvDetailBillingMonth.setText("Hóa đơn tháng " + bill.getBillingMonth() + "/" + bill.getBillingYear());
        binding.tvDetailRoom.setText(bill.getApartmentName());

        binding.tvDetailElectric.setText(moneyFormat.format(bill.getElectricityFee()));
        binding.tvDetailWater.setText(moneyFormat.format(bill.getWaterFee()));
        binding.tvDetailManagement.setText(moneyFormat.format(bill.getManagementFee()));
        binding.tvDetailSanitation.setText(moneyFormat.format(bill.getSanitationFee()));

        binding.tvDetailCreatedAt.setText(formatDate(bill.getCreatedAt()));
        binding.tvDetailDueDate.setText(formatDate(bill.getDueDate()));
        binding.tvDetailPaidAt.setText(bill.getPaidAt() != null ? formatDate(bill.getPaidAt()) : "Chưa thanh toán");

        if (bill.getStatus() == BillStatus.PAID) {
            binding.tvDetailStatus.setText("ĐÃ THANH TOÁN");
            binding.tvDetailStatus.setTextColor(Color.parseColor("#4CAF50"));
            binding.layoutPaymentNote.setVisibility(View.GONE); // Ẩn hướng dẫn nếu đã đóng tiền
        } else {
            boolean isLate = bill.getStatus() == BillStatus.LATE;
            binding.tvDetailStatus.setText(isLate ? "QUÁ HẠN" : "CHƯA THANH TOÁN");
            binding.tvDetailStatus.setTextColor(isLate ? Color.RED : Color.parseColor("#FF9800"));
            binding.layoutPaymentNote.setVisibility(View.VISIBLE);
        }
    }

    private String formatDate(String raw) {
        try {
            if (raw == null) return "--";
            return displayFormat.format(apiFormat.parse(raw));
        } catch (Exception e) { return raw; }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}