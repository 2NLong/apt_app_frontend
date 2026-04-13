package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.AdminBillAdapter;
import com.ptithcm.apt.models.bill.BillList;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminBillFragment extends Fragment {

    private static final String TAG = "AdminBillFragment";

    private int currentSelectedMonth;
    private int currentSelectedYear;
    private AdminBillViewModel viewModel;
    private AdminBillAdapter adapter;

    public AdminBillFragment() {
        // Lấy ngày tháng năm hiện tại khi khởi tạo
        Calendar calendar = Calendar.getInstance();
        currentSelectedMonth = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar bắt đầu từ 0
        currentSelectedYear = calendar.get(Calendar.YEAR);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Setup RecyclerView & Adapter
        RecyclerView recyclerView = view.findViewById(R.id.rvAdminBills);
        if (recyclerView == null) {
            Log.e(TAG, "RecyclerView rvAdminBills not found in layout!");
        }

        adapter = new AdminBillAdapter(new ArrayList<>(), new AdminBillAdapter.OnBillActionListener() {
            @Override
            public void onApprove(BillList bill) {
                Log.d(TAG, "Approving bill: " + bill.getId());
                Toast.makeText(getContext(), "Đang duyệt hóa đơn căn hộ: " + bill.getApartmentName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(BillList bill) {
                Log.d(TAG, "Item clicked: " + bill.getId());
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // 2. Setup ViewModel
        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);

        // 3. Observe Data
        viewModel.bills.observe(getViewLifecycleOwner(), bills -> {
            if (bills != null) {
                Log.d(TAG, "Received bills: " + bills.size());
                adapter.updateList(bills);
            } else {
                Log.d(TAG, "Received null bills list");
            }
        });

        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Log.e(TAG, "ViewModel error: " + errorMsg);
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

        // 4. Listeners
        Chip chipDate = view.findViewById(R.id.chipDateFilter);
        if (chipDate != null) {
            chipDate.setOnClickListener(v -> showMonthYearPicker());
            chipDate.setText("Tháng " + currentSelectedMonth + "/" + currentSelectedYear);
        }

        // 5. Load data lần đầu
        Log.d(TAG, "Fetching bills for " + currentSelectedMonth + "/" + currentSelectedYear);
        viewModel.fetchBills(currentSelectedMonth, currentSelectedYear);
    }

    private void showMonthYearPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        NumberPicker pickerMonth = dialogView.findViewById(R.id.pickerMonth);
        NumberPicker pickerYear = dialogView.findViewById(R.id.pickerYear);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        if (pickerMonth != null) {
            pickerMonth.setMinValue(1);
            pickerMonth.setMaxValue(12);
            pickerMonth.setValue(currentSelectedMonth);
        }

        if (pickerYear != null) {
            pickerYear.setMinValue(2020);
            pickerYear.setMaxValue(2030);
            pickerYear.setValue(currentSelectedYear);
        }

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (pickerMonth != null && pickerYear != null) {
                    currentSelectedMonth = pickerMonth.getValue();
                    currentSelectedYear = pickerYear.getValue();

                    View view = getView();
                    if (view != null) {
                        Chip chipDate = view.findViewById(R.id.chipDateFilter);
                        if (chipDate != null) {
                            chipDate.setText("Tháng " + currentSelectedMonth + "/" + currentSelectedYear);
                        }
                    }

                    Log.d(TAG, "Reloading bills for " + currentSelectedMonth + "/" + currentSelectedYear);
                    viewModel.fetchBills(currentSelectedMonth, currentSelectedYear);
                }
                dialog.dismiss();
            });
        }

        dialog.setContentView(dialogView);
        dialog.show();
    }
}
