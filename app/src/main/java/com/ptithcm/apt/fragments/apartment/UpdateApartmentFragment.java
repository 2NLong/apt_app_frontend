package com.ptithcm.apt.fragments.apartment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateApartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateApartmentFragment extends Fragment {

    private Long apartmentId;
    private TextInputEditText edtRoomNumber, edtFloor, edtArea;
    private Button btnSave, btnCancel;
    private Toolbar toolbar;
    MaterialButton btnViewResidents;

    private String[] statusDisplay = {"Trống", "Đang cho thuê", "Có chủ sở hữu đang ở"};
    private String[] statusRaw = {"AVAILABLE", "RENTED", "OWNED"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateApartmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateApartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // 1. Ánh xạ các view từ XML
        initViews(view);

        // 2. Thiết lập Spinner
        setupStatusSpinner();

        // 3. Thiết lập Toolbar (Nút quay lại)
        toolbar.setNavigationOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        // 4. Gọi API lấy dữ liệu chi tiết nếu có ID
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

        // 5. Sự kiện nút Lưu
        btnSave.setOnClickListener(v -> updateApartment());

        // 6. Sự kiện nút Hủy
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
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
        // 1. Lấy dữ liệu
        String roomNumber = edtRoomNumber.getText().toString().trim();
        String floorStr = edtFloor.getText().toString().trim();
        String areaStr = edtArea.getText().toString().trim();

        // 2. Kiểm tra rỗng
        if (roomNumber.isEmpty() || floorStr.isEmpty() || areaStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // 3. Chuẩn bị dữ liệu
            Apartment updateData = new Apartment();
            updateData.setRoomNumber(roomNumber);
            updateData.setFloor(Integer.parseInt(floorStr));
            updateData.setArea(Double.parseDouble(areaStr));

            // 4. Vô hiệu hóa nút Lưu để chống spam
            btnSave.setEnabled(false);
            btnSave.setText("Đang xử lý...");

            ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);

            apiService.updateApartment(apartmentId, updateData).enqueue(new Callback<Apartment>() {

                @Override
                public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        getParentFragmentManager().popBackStack();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            JSONObject jsonObject = new JSONObject(errorBody);
                            String message = jsonObject.optString("message", "Lỗi dữ liệu!");

                            // Gọi hàm Toast đỏ
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
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Số tầng hoặc diện tích không hợp lệ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        TextView text = layout.findViewById(R.id.tv_toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

         toast.setGravity(Gravity.TOP, 0, 0);

        toast.show();
    }
}