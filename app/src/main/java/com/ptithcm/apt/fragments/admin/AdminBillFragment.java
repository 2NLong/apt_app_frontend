package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.material.tabs.TabLayout;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.bill.AdminBillAdapter;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.fragments.bill.AdminBillDetailFragment;
import com.ptithcm.apt.models.bill.BillList;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.admin.AdminBillViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminBillFragment extends Fragment {

    private static final String TAG = "AdminBillFragment";

    private Integer currentSelectedMonth = null;
    private Integer currentSelectedYear = null;
    private BillStatus currentSelectedStatus = BillStatus.UNPAID; // Default status
    
    private AdminBillViewModel viewModel;
    private AdminBillAdapter adapter;
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerView;

    public AdminBillFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        recyclerView = view.findViewById(R.id.rvAdminBills);

        adapter = new AdminBillAdapter(new ArrayList<>(), new AdminBillAdapter.OnBillActionListener() {
            @Override
            public void onApprove(BillList bill) {
                Toast.makeText(getContext(), "Đang duyệt hóa đơn căn hộ: " + bill.getApartmentName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(BillList bill) {
                long billId = bill.getId();
                AdminBillDetailFragment detailFragment = AdminBillDetailFragment.newInstance(billId);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.admin_fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(AdminBillViewModel.class);

        viewModel.bills.observe(getViewLifecycleOwner(), bills -> {
            updateUI(bills);
        });

        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(getContext(), errorMsg, Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        });

        // Setup TabLayout
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutAdmin);
        if (tabLayout != null) {
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            currentSelectedStatus = BillStatus.UNPAID;
                            break;
                        case 1:
                            currentSelectedStatus = BillStatus.PAID;
                            break;
                        case 2:
                            currentSelectedStatus = BillStatus.LATE;
                            break;
                    }
                    viewModel.fetchBills(currentSelectedMonth, currentSelectedYear, currentSelectedStatus);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        Chip chipDate = view.findViewById(R.id.chipDateFilter);
        if (chipDate != null) {
            // Thiết lập sự kiện click cho Chip
            chipDate.setOnClickListener(v -> {
                if (chipDate.isChecked()) {
                    // Nếu vừa được check -> Mở picker
                    showMonthYearPicker();
                } else {
                    // Nếu vừa bị uncheck -> Reset về mặc định (Tất cả)
                    currentSelectedMonth = null;
                    currentSelectedYear = null;
                    updateChipText(chipDate);
                    viewModel.fetchBills(null, null, currentSelectedStatus);
                }
            });
            updateChipText(chipDate);
        }

        // Load data mặc định (null, null, UNPAID)
        viewModel.fetchBills(currentSelectedMonth, currentSelectedYear, currentSelectedStatus);
    }

    private void updateUI(List<BillList> bills) {
        if (bills == null || bills.isEmpty()) {
            adapter.updateList(new ArrayList<>());
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
        } else {
            adapter.updateList(bills);
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
        }
    }

    private void updateChipText(Chip chipDate) {
        if (currentSelectedMonth == null) {
            chipDate.setText("Thời gian: Tất cả");
            chipDate.setChecked(false);
        } else {
            chipDate.setText("Tháng " + currentSelectedMonth + "/" + currentSelectedYear);
            chipDate.setChecked(true);
        }
    }

    private void showMonthYearPicker() {
        Chip chipDate = getView().findViewById(R.id.chipDateFilter);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        NumberPicker pickerMonth = dialogView.findViewById(R.id.pickerMonth);
        NumberPicker pickerYear = dialogView.findViewById(R.id.pickerYear);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        Calendar calendar = Calendar.getInstance();
        int initialMonth = (currentSelectedMonth != null) ? currentSelectedMonth : (calendar.get(Calendar.MONTH) + 1);
        int initialYear = (currentSelectedYear != null) ? currentSelectedYear : calendar.get(Calendar.YEAR);

        if (pickerMonth != null) {
            pickerMonth.setMinValue(1);
            pickerMonth.setMaxValue(12);
            pickerMonth.setValue(initialMonth);
        }

        if (pickerYear != null) {
            pickerYear.setMinValue(2020);
            pickerYear.setMaxValue(2030);
            pickerYear.setValue(initialYear);
        }

        // Nếu người dùng click ra ngoài hoặc đóng dialog mà chưa chọn (và đang null) thì bỏ check chip
        dialog.setOnCancelListener(d -> {
            if (currentSelectedMonth == null && chipDate != null) {
                chipDate.setChecked(false);
            }
        });

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (pickerMonth != null && pickerYear != null) {
                    currentSelectedMonth = pickerMonth.getValue();
                    currentSelectedYear = pickerYear.getValue();
                    
                    if (chipDate != null) updateChipText(chipDate);

                    viewModel.fetchBills(currentSelectedMonth, currentSelectedYear, currentSelectedStatus);
                }
                dialog.dismiss();
            });
        }

        dialog.setContentView(dialogView);
        dialog.show();
    }
}
