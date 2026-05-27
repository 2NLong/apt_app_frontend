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

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.widget.Toolbar;

public class AddApartmentFragment extends Fragment {

    private TextInputEditText edtRoomNumber, edtFloor, edtArea;
    private Button btnSave;
    private Toolbar toolbarAdd;
    private String[] statusRaw = { "AVAILABLE" };

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public AddApartmentFragment() {
        // Required empty public constructor
    }

    public static AddApartmentFragment newInstance(String param1, String param2) {
        AddApartmentFragment fragment = new AddApartmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_apartment, container, false);

        initViews(view);
        setupSpinner();

        toolbarAdd.setNavigationOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        btnSave.setOnClickListener(v -> handleAddApartment());

        return view;
    }

    private void initViews(View view) {
        edtRoomNumber = view.findViewById(R.id.edt_add_room_number);
        edtFloor = view.findViewById(R.id.edt_add_floor);
        edtArea = view.findViewById(R.id.edt_add_area);
        btnSave = view.findViewById(R.id.btn_add_save);
        toolbarAdd = view.findViewById(R.id.toolbar_add);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statusRaw);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void handleAddApartment() {
        String room = edtRoomNumber.getText().toString().trim();
        String floor = edtFloor.getText().toString().trim();
        String area = edtArea.getText().toString().trim();
        String status = statusRaw[0];

        if (room.isEmpty() || floor.isEmpty() || area.isEmpty()) {
            showErrorToast("Vui lòng nhập đủ thông tin");
            return;
        }

        Apartment newApt = new Apartment();
        newApt.setRoomNumber(room);
        newApt.setFloor(Integer.parseInt(floor));
        newApt.setArea(Double.parseDouble(area));
        newApt.setStatus(status);

        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        apiService.createApartment(newApt).enqueue(new Callback<Apartment>() {
            @Override
            public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                if (response.isSuccessful()) {
                    showSuccessToast("Thêm căn hộ thành công!");
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
                showErrorToast("Lỗi: " + t.getMessage());
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