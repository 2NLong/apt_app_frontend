package com.ptithcm.apt.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import com.google.android.material.tabs.TabLayout;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.bill.AdminBillAdapter;
import com.ptithcm.apt.adapters.bill.UserBillAdapter;
import com.ptithcm.apt.adapters.rentinvoice.AdminRentAdapter;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.fragments.bill.UserBillDetailFragment;
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.UserBillApartmentResponse;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModelFactory;
import com.ptithcm.apt.viewmodel.bill.UserBillViewModel;
import com.ptithcm.apt.viewmodel.bill.UserBillViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BillsFragment extends Fragment {

    // Giữ nguyên các biến lọc như bên Admin
    private Integer currentSelectedMonth = null;
    private Integer currentSelectedYear = null;
    private BillStatus currentSelectedStatus = BillStatus.UNPAID;
    private Long currentSelectedApartmentId = null;
    private InvoiceType currentType = InvoiceType.SERVICE;

    private RecyclerView recyclerView;
    private LinearLayout layoutEmpty;
    private AutoCompleteTextView spinnerApartment;
    private TabLayout tabLayout;
    private Chip chipDate;
    private MaterialButtonToggleGroup toggleType;

    private UserBillViewModel viewModel;
    private UserBillAdapter userBillAdapter;


    public BillsFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bills, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupFilters();
        observeViewModel();


        // fetchUserBillsWithFullFilters();
        viewModel.fetchMyApartments();
        viewModel.fetchMyBills(null, null, null, BillStatus.UNPAID);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.rvUserBills);
        layoutEmpty = view.findViewById(R.id.layoutUserEmpty);
        spinnerApartment = view.findViewById(R.id.spinnerUserApartmentFilter);
        tabLayout = view.findViewById(R.id.tabLayoutUser);
        chipDate = view.findViewById(R.id.chipDateFilterUser);
        toggleType = view.findViewById(R.id.toggleUserBillType);
    }

    private void initViewModel() {
        UserBillViewModelFactory factory = new UserBillViewModelFactory();
        viewModel = new ViewModelProvider(this, factory).get(UserBillViewModel.class);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userBillAdapter = new UserBillAdapter(new ArrayList<>(), bill -> {
            if (currentType == InvoiceType.SERVICE) {
                openBillDetail(bill.getId());
            } else {
                openRentDetail(bill.getId());
            }
        });

        // Gán vào RecyclerView
        recyclerView.setAdapter(userBillAdapter);
//        recyclerView.setAdapter(serviceAdapter);
    }

    private void setupFilters() {
        // 1. Toggle Loại hóa đơn
        toggleType.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btnTypeService) {
                    currentType = InvoiceType.SERVICE;
                    recyclerView.setAdapter(userBillAdapter);
                } else {
                    currentType = InvoiceType.RENT;
//                    recyclerView.setAdapter(rentAdapter);
                }
                fetchUserBillsWithFullFilters();
            }
        });

        // Trong setupFilters()
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: // Cần thanh toán
                        currentSelectedStatus = BillStatus.UNPAID;
                        break;
                    case 1: // Trễ hạn
                        currentSelectedStatus = BillStatus.LATE;
                        break;
                    case 2: // Tất cả
                        currentSelectedStatus = null;
                        break;
                }
                fetchUserBillsWithFullFilters();
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // 3. Chọn căn hộ (Spinner)
        spinnerApartment.setOnItemClickListener((parent, v, position, id) -> {
            UserBillApartmentResponse selected = (UserBillApartmentResponse) parent.getItemAtPosition(position);
            currentSelectedApartmentId = selected.getApartmentId();
            fetchUserBillsWithFullFilters();
        });

        // 4. GIỮ NGUYÊN LOGIC CHIP DATE NHƯ ADMIN
        updateChipText();
        chipDate.setOnClickListener(v -> {
            if (chipDate.isChecked()) {
                showMonthYearPicker();
            } else {
                currentSelectedMonth = null;
                currentSelectedYear = null;
                updateChipText();
                fetchUserBillsWithFullFilters();
            }
        });
    }

    private void updateChipText() {
        if (currentSelectedMonth == null) {
            chipDate.setText("Thời gian: Tất cả");
            chipDate.setChecked(false);
        } else {
            chipDate.setText("Tháng " + currentSelectedMonth + "/" + currentSelectedYear);
            chipDate.setChecked(true);
        }
    }

    private void showMonthYearPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        NumberPicker pickerMonth = dialogView.findViewById(R.id.pickerMonth);
        NumberPicker pickerYear = dialogView.findViewById(R.id.pickerYear);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        Calendar cal = Calendar.getInstance();
        pickerMonth.setMinValue(1);
        pickerMonth.setMaxValue(12);
        pickerMonth.setValue(currentSelectedMonth != null ? currentSelectedMonth : cal.get(Calendar.MONTH) + 1);

        pickerYear.setMinValue(2020);
        pickerYear.setMaxValue(2030);
        pickerYear.setValue(currentSelectedYear != null ? currentSelectedYear : cal.get(Calendar.YEAR));

        btnConfirm.setOnClickListener(v -> {
            currentSelectedMonth = pickerMonth.getValue();
            currentSelectedYear = pickerYear.getValue();
            updateChipText();
            fetchUserBillsWithFullFilters();
            dialog.dismiss();
        });

        dialog.setOnCancelListener(d -> {
            if (currentSelectedMonth == null) chipDate.setChecked(false);
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }

    private void fetchUserBillsWithFullFilters() {

        if (currentType == InvoiceType.SERVICE) {

            viewModel.fetchMyBills(currentSelectedMonth, currentSelectedYear, currentSelectedApartmentId, currentSelectedStatus);
        } else {

        }

        // Ví dụ: viewModel.fetchUserBills(currentSelectedMonth, currentSelectedYear, currentSelectedApartmentId, currentSelectedStatus);
    }

    private void observeViewModel() {

        viewModel.myApartments.observe(getViewLifecycleOwner(), apartments -> {
            if (apartments != null) {
                List<UserBillApartmentResponse> filterList = new ArrayList<>();

                // Thêm lựa chọn "Tất cả căn hộ"
                UserBillApartmentResponse allOption = new UserBillApartmentResponse();
                allOption.setRoomNumber("Tất cả căn hộ");
                allOption.setApartmentId(null); // Để null để API lấy tất cả
                filterList.add(allOption);

                filterList.addAll(apartments);

                ArrayAdapter<UserBillApartmentResponse> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        filterList);
                spinnerApartment.setAdapter(adapter);

                // Nếu chỉ có 1 căn, tự động chọn và khóa lại (không cho chọn "Tất cả")
                if (apartments.size() == 1) {
                    currentSelectedApartmentId = apartments.get(0).getApartmentId();
                    spinnerApartment.setText(apartments.get(0).getRoomNumber(), false);
                    spinnerApartment.setEnabled(false);
                } else {
                    // Mặc định hiển thị "Tất cả căn hộ" khi mới load
                    spinnerApartment.setText("Tất cả căn hộ", false);
                    spinnerApartment.setEnabled(true);
                }
            }
        });

        viewModel.bills.observe(getViewLifecycleOwner(), bills -> {
            if (bills == null || bills.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            } else {
                userBillAdapter.updateList(bills);
                recyclerView.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
            }
        });
        viewModel.error.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        // Theo dõi lỗi (nếu có)


        // ... Observe các LiveData bills và rentInvoices tương tự Admin ...
    }

    private void openBillDetail(long billId) {
        UserBillDetailFragment detailFragment = UserBillDetailFragment.newInstance(billId);

        getParentFragmentManager().beginTransaction()
                // THAY R.id.admin_fragment_container bằng ID container trong layout của bạn
                .replace(R.id.main_fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    private void openRentDetail(long rentId) {
//        UserRentDetailFragment detailFragment = UserRentDetailFragment.newInstance(rentId);
//
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.fragment_container, detailFragment)
//                .addToBackStack(null)
//                .commit();
    }

    private enum InvoiceType { SERVICE, RENT }
}