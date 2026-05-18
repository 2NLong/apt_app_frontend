package com.ptithcm.apt.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.models.contract.ContractRequest;
import com.ptithcm.apt.models.resident.Resident;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.api.ContractApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateContractFragment extends Fragment {

    private RadioGroup rgRole;
    private TextInputEditText edtRoomNumber, edtFullName, edtCccd, edtEmail, edtPrice;
    private TextInputEditText edtDob, edtStartDate, edtEndDate;
    private Button btnCreate;
    private TextInputEditText edtPhone, edtDepositAmount;
    private Long apartmentId = -1L;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateContractFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateContractFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateContractFragment newInstance(String param1, String param2) {
        CreateContractFragment fragment = new CreateContractFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contract, container, false);

        initViews(view);
        setupEvents();

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

        btnCreate = view.findViewById(R.id.btn_create_contract);
    }

    private void setupEvents() {
        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_owner) {
                edtPrice.setHint("Giá trị căn hộ (Mua đứt)");
                edtEndDate.setText(""); // Mua đứt thường không có ngày kết thúc
                edtEndDate.setEnabled(false);
            } else {
                edtPrice.setHint("Tiền thuê hàng tháng");
                edtEndDate.setEnabled(true);
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

        if (roomNumber.isEmpty() || fullName.isEmpty() || cccd.isEmpty() || email.isEmpty() || dob.isEmpty() || startDate.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ các thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApartmentApiService aptApi = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        aptApi.searchApartments(roomNumber).enqueue(new Callback<List<Apartment>>() {
            @Override
            public void onResponse(Call<List<Apartment>> call, Response<List<Apartment>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    Long apartmentId = response.body().get(0).getId();

                    submitContractToBackend(apartmentId);
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy phòng số " + roomNumber, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Apartment>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối khi tìm phòng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitContractToBackend(Long apartmentId) {
        String role = rgRole.getCheckedRadioButtonId() == R.id.rb_owner ? "OWNER" : "TENANT";

        String priceStr = edtPrice.getText().toString().trim();
        BigDecimal price = priceStr.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceStr);

        String depositStr = edtDepositAmount.getText().toString().trim();
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
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Lập hợp đồng thành công!", Toast.LENGTH_LONG).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Lỗi lập hợp đồng! Xem lại trạng thái phòng.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Resident> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}