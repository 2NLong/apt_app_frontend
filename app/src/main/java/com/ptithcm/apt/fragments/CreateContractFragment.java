package com.ptithcm.apt.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.models.contract.ContractRequest;
import com.ptithcm.apt.models.resident.Resident;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.api.ContractApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContractFragment extends Fragment {

    private RadioGroup rgRole;
    private TextInputEditText edtRoomNumber, edtFullName, edtCccd, edtEmail, edtPrice;
    private TextInputEditText edtDob, edtStartDate, edtEndDate;
    private TextInputEditText edtPhone, edtDepositAmount;
    private Button btnCreate;

    private TextInputLayout tilPrice, tilDeposit, tilEndDate;
    private TextInputLayout tilCccd, tilEmail, tilDob;

    public CreateContractFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contract, container, false);

        initViews(view);
        setupEvents(view);

        return view;
    }

    private void initViews(View view) {
        rgRole = view.findViewById(R.id.rg_role);

        edtRoomNumber = view.findViewById(R.id.edt_roomNumber);
        edtFullName = view.findViewById(R.id.edt_fullName);
        edtCccd = view.findViewById(R.id.edt_cccd);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPrice = view.findViewById(R.id.edt_rentalPrice);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtDepositAmount = view.findViewById(R.id.edt_depositAmount);
        edtDob = view.findViewById(R.id.edt_dob);
        edtStartDate = view.findViewById(R.id.edt_startDate);
        edtEndDate = view.findViewById(R.id.edt_endDate);

        tilPrice = view.findViewById(R.id.til_price);
        tilDeposit = view.findViewById(R.id.til_deposit);
        tilEndDate = view.findViewById(R.id.til_end_date);

        tilCccd = view.findViewById(R.id.til_cccd);
        tilEmail = view.findViewById(R.id.til_email);
        tilDob = view.findViewById(R.id.til_dob);

        btnCreate = view.findViewById(R.id.btn_create_contract);
    }

    private void setupEvents(View view) {
        view.findViewById(R.id.toolbar_create_contract).setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_owner) {
                tilPrice.setHint("Giá mua căn hộ (VNĐ)");
                tilDeposit.setVisibility(View.GONE);
                tilEndDate.setVisibility(View.GONE);
                edtDepositAmount.setText("");
                edtEndDate.setText("");
            } else {
                tilPrice.setHint("Tiền thuê mỗi tháng (VNĐ)");
                tilDeposit.setVisibility(View.VISIBLE);
                tilEndDate.setVisibility(View.VISIBLE);
            }
        });

        edtDob.setOnClickListener(v -> showDatePicker(edtDob));
        edtStartDate.setOnClickListener(v -> showDatePicker(edtStartDate));
        edtEndDate.setOnClickListener(v -> showDatePicker(edtEndDate));

        btnCreate.setOnClickListener(v -> processContractCreation());
    }

    private void showDatePicker(TextInputEditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            String date = String.format("%02d-%02d-%04d", dayOfMonth, month + 1, year);
            editText.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void processContractCreation() {
        String roomNumber = edtRoomNumber.getText().toString().trim();
        String fullName = edtFullName.getText().toString().trim();
        String cccd = edtCccd.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String startDate = edtStartDate.getText().toString().trim();

        String priceStr = edtPrice.getText().toString().trim().replaceAll("[,.]", "");
        String depositStr = edtDepositAmount.getText().toString().trim().replaceAll("[,.]", "");
        String endDateStr = edtEndDate.getText().toString().trim();

        tilCccd.setError(null);
        tilEmail.setError(null);
        tilDob.setError(null);

        if (roomNumber.isEmpty() || fullName.isEmpty() || cccd.isEmpty() || email.isEmpty() || dob.isEmpty() || startDate.isEmpty()) {
            showErrorToast("Vui lòng nhập đủ các thông tin bắt buộc!");
            return;
        }

        boolean isValid = true;

        if (!cccd.matches("\\d{12}")) {
            tilCccd.setError("CCCD phải bao gồm đúng 12 chữ số!");
            edtCccd.requestFocus();
            isValid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Vui lòng nhập đúng định dạng Email!");
            edtEmail.requestFocus();
            isValid = false;
        }

        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
            java.util.Date dobDate = sdf.parse(dob);
            java.util.Calendar birthCal = java.util.Calendar.getInstance();
            birthCal.setTime(dobDate);

            java.util.Calendar today = java.util.Calendar.getInstance();

            int age = today.get(java.util.Calendar.YEAR) - birthCal.get(java.util.Calendar.YEAR);
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < birthCal.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--;
            }

            if (age < 18) {
                tilDob.setError("Người đại diện lập hợp đồng phải từ đủ 18 tuổi!");
                edtDob.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            tilDob.setError("Ngày sinh không hợp lệ!");
            isValid = false;
        }

        boolean isOwner = rgRole.getCheckedRadioButtonId() == R.id.rb_owner;

        if (isOwner) {
            if (priceStr.isEmpty()) {
                tilPrice.setError("Vui lòng nhập giá mua căn hộ!");
                if (isValid) edtPrice.requestFocus();
                isValid = false;
            }
        } else {
            if (priceStr.isEmpty()) {
                tilPrice.setError("Vui lòng nhập tiền thuê mỗi tháng!");
                if (isValid) edtPrice.requestFocus();
                isValid = false;
            }
            if (depositStr.isEmpty()) {
                tilDeposit.setError("Vui lòng nhập số tiền đặt cọc!");
                if (isValid) edtDepositAmount.requestFocus();
                isValid = false;
            }
            if (endDateStr.isEmpty()) {
                tilEndDate.setError("Vui lòng chọn ngày kết thúc hợp đồng!");
                isValid = false;
            } else {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault());
                    java.util.Date start = sdf.parse(startDate);
                    java.util.Date end = sdf.parse(endDateStr);
                    if (!end.after(start)) {
                        tilEndDate.setError("Ngày kết thúc phải sau ngày bắt đầu!");
                        isValid = false;
                    }
                } catch (Exception e) {
                    tilEndDate.setError("Ngày kết thúc không hợp lệ!");
                    isValid = false;
                }
            }
        }

        if (!isValid) {
            return;
        }

        btnCreate.setEnabled(false);
        btnCreate.setText("Đang xử lý...");

        ApartmentApiService aptApi = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        aptApi.searchApartments(roomNumber).enqueue(new Callback<List<Apartment>>() {
            @Override
            public void onResponse(Call<List<Apartment>> call, Response<List<Apartment>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Long apartmentId = response.body().get(0).getId();
                    submitContractToBackend(apartmentId);
                } else {
                    btnCreate.setEnabled(true);
                    btnCreate.setText("LẬP HỢP ĐỒNG");
                    showErrorToast("Không tìm thấy phòng số " + roomNumber);
                }
            }

            @Override
            public void onFailure(Call<List<Apartment>> call, Throwable t) {
                btnCreate.setEnabled(true);
                btnCreate.setText("LẬP HỢP ĐỒNG");
                showErrorToast("Lỗi kết nối khi tìm phòng: " + t.getMessage());
            }
        });
    }

    private void submitContractToBackend(Long apartmentId) {
        String role = rgRole.getCheckedRadioButtonId() == R.id.rb_owner ? "OWNER" : "TENANT";

        String priceStr = edtPrice.getText().toString().trim().replaceAll("[,.]", "");
        BigDecimal price = priceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceStr);

        String depositStr = edtDepositAmount.getText().toString().trim().replaceAll("[,.]", "");
        BigDecimal deposit = depositStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(depositStr);

        String endDateStr = edtEndDate.getText().toString().trim();
        String endDate = endDateStr.isEmpty() ? null : endDateStr;

        ContractRequest request = new ContractRequest(
                apartmentId, role,
                edtEmail.getText().toString().trim(),
                edtCccd.getText().toString().trim(),
                edtFullName.getText().toString().trim(),
                edtDob.getText().toString().trim(),
                edtPhone.getText().toString().trim(),
                edtStartDate.getText().toString().trim(),
                endDate,
                price,
                deposit
        );

        ContractApiService contractApi = RetrofitClient.getInstance().createService(ContractApiService.class);

        contractApi.createContract(request).enqueue(new Callback<Resident>() {
            @Override
            public void onResponse(Call<Resident> call, Response<Resident> response) {
                btnCreate.setEnabled(true);
                btnCreate.setText("LẬP HỢP ĐỒNG");

                if (response.isSuccessful()) {
                    showSuccessToast("Lập hợp đồng thành công!");
                    getParentFragmentManager().popBackStack();
                } else {
                    try {
                        String errorMsg = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(errorMsg);
                        String message = jsonObject.optString("message", "Dữ liệu hợp đồng không hợp lệ!");
                        showErrorToast(message);
                    } catch (Exception e) {
                        showErrorToast("Lỗi lập hợp đồng! Xem lại trạng thái phòng.");
                    }
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                btnCreate.setEnabled(true);
                btnCreate.setText("LẬP HỢP ĐỒNG");
                showErrorToast("Lỗi kết nối mạng: " + t.getMessage());
            }
        });
    }

    private void showSuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast_success, null);

        TextView text = layout.findViewById(R.id.tv_toast_message_success);
        text.setText(message);

        Toast toast = new Toast(requireActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_toast_error, null);

        TextView text = layout.findViewById(R.id.tv_toast_message_error);
        text.setText(message);

        Toast toast = new Toast(requireActivity().getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}