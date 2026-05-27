package com.ptithcm.apt.fragments.apartment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.fragments.ResidentListInApartmentFragment;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Call;

public class UpdateApartmentFragment extends Fragment {

    private Long apartmentId;
    private TextInputEditText edtRoomNumber, edtFloor, edtArea;
    private Button btnSave, btnCancel;
    private Toolbar toolbar;
    MaterialButton btnViewResidents;

    private String[] statusDisplay = {"Trống", "Đang cho thuê", "Có chủ sở hữu đang ở"};
    private String[] statusRaw = {"AVAILABLE", "RENTED", "OWNED"};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public UpdateApartmentFragment() {
        // Required empty public constructor
    }

    public static UpdateApartmentFragment newInstance(String param1, String param2) {
        UpdateApartmentFragment fragment = new UpdateApartmentFragment();
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
            apartmentId = getArguments().getLong("APARTMENT_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_apartment, container, false);

        initViews(view);
        setupStatusSpinner();

        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        if (apartmentId != null) {
            fetchApartmentDetails(apartmentId);
        }

        btnViewResidents = view.findViewById(R.id.btn_view_residents);

        btnViewResidents.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putLong("APARTMENT_ID", apartmentId);

            ResidentListInApartmentFragment residentFragment = new ResidentListInApartmentFragment();
            residentFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, residentFragment)
                    .addToBackStack(null)
                    .commit();
        });

        btnSave.setOnClickListener(v -> updateApartment());
        btnCancel.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        return view;
    }

    private void initViews(View view) {
        toolbar = view.findViewById(R.id.toolbar_update);
        edtRoomNumber = view.findViewById(R.id.edt_update_room_number);
        edtFloor = view.findViewById(R.id.edt_update_floor);
        edtArea = view.findViewById(R.id.edt_update_area);
        btnSave = view.findViewById(R.id.btn_update_save);
        btnCancel = view.findViewById(R.id.btn_update_cancel);
    }

    private void setupStatusSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, statusDisplay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void fetchApartmentDetails(Long id) {
        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        apiService.getApartmentById(id).enqueue(new Callback<Apartment>() {
            @Override
            public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fillDataToView(response.body());
                }
            }

            @Override
            public void onFailure(Call<Apartment> call, Throwable t) {
                showErrorToast("Lỗi tải dữ liệu: " + t.getMessage());
            }
        });
    }

    private void fillDataToView(Apartment apartment) {
        edtRoomNumber.setText(apartment.getRoomNumber());
        edtFloor.setText(String.valueOf(apartment.getFloor()));
        edtArea.setText(String.valueOf(apartment.getArea()));

        for (int i = 0; i < statusRaw.length; i++) {
            if (statusRaw[i].equals(apartment.getStatus())) {
                break;
            }
        }
    }

    private void updateApartment() {
        String roomNumber = edtRoomNumber.getText().toString().trim();
        String floorStr = edtFloor.getText().toString().trim();
        String areaStr = edtArea.getText().toString().trim();

        if (roomNumber.isEmpty() || floorStr.isEmpty() || areaStr.isEmpty()) {
            showErrorToast("Vui lòng nhập đủ thông tin");
            return;
        }

        try {
            Apartment updateData = new Apartment();
            updateData.setRoomNumber(roomNumber);
            updateData.setFloor(Integer.parseInt(floorStr));
            updateData.setArea(Double.parseDouble(areaStr));

            btnSave.setEnabled(false);
            btnSave.setText("Đang xử lý...");

            ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);

            apiService.updateApartment(apartmentId, updateData).enqueue(new Callback<Apartment>() {

                @Override
                public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    if (response.isSuccessful()) {
                        showSuccessToast("Cập nhật thành công!");
                        getParentFragmentManager().popBackStack();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Lỗi dữ liệu!");
                            showErrorToast(message);
                        } catch (Exception e) {
                            showErrorToast("Lỗi hệ thống!");
                        }
                    }
                }

                @Override
                public void onFailure(Call<Apartment> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    showErrorToast("Lỗi kết nối: " + t.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            showErrorToast("Số tầng hoặc diện tích không hợp lệ!");
        }
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