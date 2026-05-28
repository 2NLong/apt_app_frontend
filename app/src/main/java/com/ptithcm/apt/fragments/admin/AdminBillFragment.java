package com.ptithcm.apt.fragments.admin;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.bill.AdminBillAdapter;
import com.ptithcm.apt.adapters.rentinvoice.AdminRentAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.enums.BillStatus;
import com.ptithcm.apt.fragments.bill.AdminBillDetailFragment;
import com.ptithcm.apt.fragments.bill.AdminCreateBillFragment;
import com.ptithcm.apt.fragments.rentinvoice.AdminRentInvoiceDetailFragment;
import com.ptithcm.apt.models.rentinvoice.response.RentInvoiceListResponse;
import com.ptithcm.apt.models.bill.response.BillApartmentResponse;
import com.ptithcm.apt.models.bill.response.BillListResponse;
import com.ptithcm.apt.models.auth.response.PageResponse;
import com.ptithcm.apt.utils.DialogUtils;
import com.ptithcm.apt.utils.ToastUtils;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModel;
import com.ptithcm.apt.viewmodel.bill.AdminBillViewModelFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminBillFragment extends Fragment {

    private Integer currentSelectedMonth = null;
    private Integer currentSelectedYear = null;
    private BillStatus currentSelectedStatus = BillStatus.UNPAID;
    private Long currentSelectedApartmentId = null;
    private String currentSearchRoomNumber = null;

    // Pagination variables
    private int currentPage = 0;
    private final int PAGE_SIZE = 5;
    private int totalPages = 1;

    private AdminBillViewModel viewModel;
    private AdminBillAdapter adapter;

    // Views
    private LinearLayout layoutEmpty;
    private RecyclerView recyclerView;
    private FloatingActionButton fabAdd;
    private AutoCompleteTextView spinnerApartmentFilter;
    private TextInputEditText etSearchRoom;
    private TabLayout tabLayout;
    private Chip chipDate;
    private InvoiceType currentType = InvoiceType.SERVICE;
    private AdminRentAdapter rentAdapter;
    private MaterialCardView cardService, cardRent;
    private TextView tvServiceBill, tvRentBill;

    // Pagination Views
    private View layoutPagination;
    private View btnPrevPage;
    private View btnNextPage;
    private TextView tvPageInfo;

    public AdminBillFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initViewModel();
        setupRecyclerView();
        setupApartmentFilter();
        setupSearchFilter();
        setupTabsAndChips();
        setupListeners();
        observeViewModel();

        // Load dữ liệu ban đầu
        viewModel.fetchApartmentsForBill();
        fetchBillsWithFullFilters();
    }

    private void initViews(View view) {
        cardService = view.findViewById(R.id.cardServiceBill);
        cardRent = view.findViewById(R.id.cardRentBill);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        recyclerView = view.findViewById(R.id.rvAdminBills);
        fabAdd = view.findViewById(R.id.fabAddBill);
        spinnerApartmentFilter = view.findViewById(R.id.spinnerApartmentFilter);
        etSearchRoom = view.findViewById(R.id.etSearchRoom);
        tabLayout = view.findViewById(R.id.tabLayoutAdmin);
        chipDate = view.findViewById(R.id.chipDateFilter);
        tvServiceBill = view.findViewById(R.id.tvServiceBill);
        tvRentBill = view.findViewById(R.id.tvRentBill);

        // Pagination Views
        layoutPagination = view.findViewById(R.id.layoutPagination);
        btnPrevPage = view.findViewById(R.id.btnPrevPage);
        btnNextPage = view.findViewById(R.id.btnNextPage);
        tvPageInfo = view.findViewById(R.id.tvPageInfo);
    }

    private void initViewModel() {
        AdminBillViewModelFactory factory = new AdminBillViewModelFactory();
        viewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) factory).get(AdminBillViewModel.class);
    }

    private void setupRecyclerView() {
        adapter = new AdminBillAdapter(new ArrayList<>(),
                new AdminBillAdapter.OnBillActionListener() {
                    @Override
                    public void onApprove(BillListResponse bill) {
                        DialogUtils.showConfirmDialog(requireContext(),
                                "Xác nhận duyệt phí",
                                "Xác nhận căn hộ " + bill.getApartmentName() + " đã đóng tổng cộng " + formatMoney(
                                        bill.getTotalAmount()) + "?",
                                () -> {
                                    viewModel.approveBill(
                                            bill.getId());
                                });
                    }

                    @Override
                    public void onItemClick(BillListResponse bill) {
                        openBillDetail(bill.getId());
                    }
                });

        rentAdapter = new AdminRentAdapter(new ArrayList<>(),
                new AdminRentAdapter.OnRentActionListener() {
                    @Override
                    public void onApprove(RentInvoiceListResponse rent) {

                        DialogUtils.showConfirmDialog(requireContext(),
                                "Duyệt tiền thuê",
                                "Xác nhận căn hộ " + rent.getApartmentName() + " đã đóng " + formatMoney(
                                        rent.getRentAmount()) + "?",
                                () -> {
                                    viewModel.approveRentInvoice(
                                            rent.getId());
                                });
                    }

                    @Override
                    public void onItemClick(RentInvoiceListResponse bill) {
                        openRentDetail(bill.getId());
                    }
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupApartmentFilter() {
        spinnerApartmentFilter.setOnItemClickListener((parent, v, position, id) -> {
            BillApartmentResponse selected = (BillApartmentResponse) parent.getItemAtPosition(
                    position);
            currentSelectedApartmentId = selected.getId();
            resetPageAndFetch();
        });

        // Sự kiện xóa text để reset filter
        spinnerApartmentFilter.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (s.toString().isEmpty() && currentSelectedApartmentId != null) {
                    currentSelectedApartmentId = null;
                    resetPageAndFetch();
                }
            }
        });
    }

    private final android.os.Handler searchHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable searchRunnable;

    private void setupSearchFilter() {
        etSearchRoom.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(android.text.Editable s) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    currentSearchRoomNumber = query.isEmpty() ? null : query;
                    resetPageAndFetch();
                };
                searchHandler.postDelayed(searchRunnable, 500); // Debounce search for 500ms
            }
        });

        etSearchRoom.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                String query = etSearchRoom.getText().toString().trim();
                currentSearchRoomNumber = query.isEmpty() ? null : query;
                resetPageAndFetch();
                android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) 
                        requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etSearchRoom.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
    }

    private void setupTabsAndChips() {
        // Cấu hình Tabs
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
                    resetPageAndFetch();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });
        }

        // Cấu hình Chip ngày tháng
        if (chipDate != null) {
            updateChipText();
            chipDate.setOnClickListener(v -> {
                if (chipDate.isChecked()) {
                    showMonthYearPicker();
                } else {
                    currentSelectedMonth = null;
                    currentSelectedYear = null;
                    updateChipText();
                    resetPageAndFetch();
                }
            });
        }
    }

    private void setupListeners() {
        cardService.setOnClickListener(v -> {
            currentType = InvoiceType.SERVICE;
            updateToggleUI();
            recyclerView.setAdapter(adapter);
            resetPageAndFetch();
        });

        cardRent.setOnClickListener(v -> {
            currentType = InvoiceType.RENT;
            updateToggleUI();
            recyclerView.setAdapter(rentAdapter);
            resetPageAndFetch();
        });

        fabAdd.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,
                    new AdminCreateBillFragment()).addToBackStack(
                    null).commit();
        });

        // Pagination buttons
        btnPrevPage.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                fetchBillsWithFullFilters();
            }
        });

        btnNextPage.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                fetchBillsWithFullFilters();
            }
        });
    }

    private void updateToggleUI() {
        // Lấy màu từ resources
        int colorPrimary = ContextCompat.getColor(requireContext(), R.color.primary);
        int colorWhite = ContextCompat.getColor(requireContext(), R.color.white);
        int colorBody = ContextCompat.getColor(requireContext(), R.color.text_body);
        int colorStrokeDefault = Color.parseColor("#E0E0E0");
        int colorBackgroundSelected = Color.parseColor("#FDF2F3");

        if (currentType == InvoiceType.SERVICE) {
            cardService.setStrokeWidth(4);
            cardService.setStrokeColor(colorPrimary);
            cardService.setCardBackgroundColor(colorBackgroundSelected);
            tvServiceBill.setTextColor(colorPrimary);

            cardRent.setStrokeWidth(2);
            cardRent.setStrokeColor(colorStrokeDefault);
            cardRent.setCardBackgroundColor(colorWhite);
            tvRentBill.setTextColor(colorBody);
        } else {
            cardRent.setStrokeWidth(4);
            cardRent.setStrokeColor(colorPrimary);
            cardRent.setCardBackgroundColor(colorBackgroundSelected);
            tvRentBill.setTextColor(colorPrimary);

            cardService.setStrokeWidth(2);
            cardService.setStrokeColor(colorStrokeDefault);
            cardService.setCardBackgroundColor(colorWhite);
            tvServiceBill.setTextColor(colorBody);
        }
    }

    private void observeViewModel() {
        // 1. Quan sát hóa đơn dịch vụ (Điện, nước...)
        viewModel.bills.observe(getViewLifecycleOwner(), bills -> {
            // Chỉ cập nhật UI nếu đang ở chế độ xem Dịch vụ
            if (currentType == InvoiceType.SERVICE) {
                updateUI(bills);
            }
        });

        // 2. Quan sát hóa đơn tiền thuê (MỚI)
        viewModel.rentInvoices.observe(getViewLifecycleOwner(), rents -> {
            // Chỉ cập nhật UI nếu đang ở chế độ xem Tiền thuê
            if (currentType == InvoiceType.RENT) {
                updateRentUI(rents);
            }
        });

        // 3. Quan sát danh sách căn hộ (Dùng chung cho cả 2 bộ lọc)
        viewModel.billApartments.observe(getViewLifecycleOwner(), apartments -> {
            if (apartments != null) {
                List<BillApartmentResponse> filterList = new ArrayList<>();
                BillApartmentResponse allOption = new BillApartmentResponse();
                allOption.setId(null);
                allOption.setRoomNumber("Tất cả căn hộ");
                filterList.add(allOption);
                filterList.addAll(apartments);

                ArrayAdapter<BillApartmentResponse> apartmentAdapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        filterList);
                spinnerApartmentFilter.setAdapter(apartmentAdapter);

                if (currentSelectedApartmentId == null) {
                    spinnerApartmentFilter.setText(allOption.getRoomNumber(), false);
                }
            }
        });

        viewModel.updateStatusSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null && isSuccess) {
                // Thông báo thành công
                ToastUtils.showSuccessToast(requireContext(), "Duyệt hóa đơn thành công!");

                // QUAN TRỌNG: Gọi lại hàm fetch dữ liệu của bạn để danh sách cập nhật mới nhất
                // Ví dụ: fetchBills(currentMonth, currentYear, null, currentStatus);
                fetchBillsWithFullFilters();
            }
        });

        viewModel.updateRentSuccess.observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess != null && isSuccess) {
                ToastUtils.showSuccessToast(requireContext(), "Duyệt tiền thuê thành công!");
                fetchBillsWithFullFilters(); // Fetch lại danh sách để cập nhật UI
            }
        });

        // 4. Quan sát lỗi
        viewModel.error.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                ToastUtils.showErrorToast(requireContext(), errorMsg);
                // Xóa danh sách hiện tại tùy theo mode
                if (currentType == InvoiceType.SERVICE) updateUI(null);
                else updateRentUI(null);
            }
        });
    }

    private void resetPageAndFetch() {
        currentPage = 0;
        fetchBillsWithFullFilters();
    }

    private void fetchBillsWithFullFilters() {
        if (currentType == InvoiceType.SERVICE) {
            viewModel.fetchBills(currentSelectedMonth,
                    currentSelectedYear,
                    currentSelectedApartmentId,
                    currentSelectedStatus,
                    currentSearchRoomNumber,
                    currentPage,
                    PAGE_SIZE);
        } else {
            viewModel.fetchRentInvoices(currentSelectedMonth,
                    currentSelectedYear,
                    currentSelectedApartmentId,
                    currentSelectedStatus,
                    currentSearchRoomNumber,
                    currentPage,
                    PAGE_SIZE);
        }
    }

    private void updateUI(PageResponse<BillListResponse> pageResponse) {
        if (pageResponse == null || pageResponse.getContent() == null || pageResponse.getContent().isEmpty()) {
            adapter.updateList(new ArrayList<>());
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            totalPages = 1;
            currentPage = 0;
            updatePaginationUI();
        } else {
            adapter.updateList(pageResponse.getContent());
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            totalPages = pageResponse.getTotalPages();
            currentPage = pageResponse.getPage();
            updatePaginationUI();
        }
    }

    private void updateRentUI(PageResponse<RentInvoiceListResponse> pageResponse) {
        if (pageResponse == null || pageResponse.getContent() == null || pageResponse.getContent().isEmpty()) {
            rentAdapter.updateList(new ArrayList<>());
            recyclerView.setVisibility(View.GONE);
            layoutEmpty.setVisibility(View.VISIBLE);
            totalPages = 1;
            currentPage = 0;
            updatePaginationUI();
        } else {
            rentAdapter.updateList(pageResponse.getContent());
            recyclerView.setVisibility(View.VISIBLE);
            layoutEmpty.setVisibility(View.GONE);
            totalPages = pageResponse.getTotalPages();
            currentPage = pageResponse.getPage();
            updatePaginationUI();
        }
    }

    private void updatePaginationUI() {
        if (totalPages <= 1) {
            layoutPagination.setVisibility(View.GONE);
        } else {
            layoutPagination.setVisibility(View.VISIBLE);
            tvPageInfo.setText("Trang " + (currentPage + 1) + " / " + totalPages);
            btnPrevPage.setEnabled(currentPage > 0);
            btnNextPage.setEnabled(currentPage < totalPages - 1);
            
            // Adjust alpha to visually show enabled/disabled states
            btnPrevPage.setAlpha(currentPage > 0 ? 1.0f : 0.4f);
            btnNextPage.setAlpha(currentPage < totalPages - 1 ? 1.0f : 0.4f);
        }
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

    private void openBillDetail(long billId) {
        AdminBillDetailFragment detailFragment = AdminBillDetailFragment.newInstance(billId);
        getParentFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,
                detailFragment).addToBackStack(null).commit();
    }

    private void openRentDetail(long rentId) {
        AdminRentInvoiceDetailFragment detailFragment = AdminRentInvoiceDetailFragment.newInstance(
                rentId);
        getParentFragmentManager().beginTransaction().replace(R.id.admin_fragment_container,
                detailFragment).addToBackStack(null).commit();
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
            resetPageAndFetch();
            dialog.dismiss();
        });

        dialog.setOnCancelListener(d -> {
            if (currentSelectedMonth == null) chipDate.setChecked(false);
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }

    private String formatMoney(java.math.BigDecimal amount) {
        if (amount == null) return "0đ";
        java.text.DecimalFormat formatter = new java.text.DecimalFormat("#,###đ");
        return formatter.format(amount);
    }

    private enum InvoiceType {SERVICE, RENT}
}