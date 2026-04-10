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

import com.google.android.material.textfield.TextInputEditText;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddApartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddApartmentFragment extends Fragment {

    private TextInputEditText edtRoomNumber, edtFloor, edtArea;
    private Spinner spinnerStatus;
    private Button btnSave;
    private String[] statusRaw = {"AVAILABLE", "RENTED", "OWNED"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddApartmentFragment() {
        // Required empty public constructor
    }

    private void initViews(View view) {
        edtRoomNumber = view.findViewById(R.id.edt_add_room_number);
        edtFloor = view.findViewById(R.id.edt_add_floor);
        edtArea = view.findViewById(R.id.edt_add_area);
        spinnerStatus = view.findViewById(R.id.spinner_add_status);
        btnSave = view.findViewById(R.id.btn_add_save);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                statusRaw
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddApartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddApartmentFragment newInstance(String param1, String param2) {
        AddApartmentFragment fragment = new AddApartmentFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_apartment, container, false);

        initViews(view);
        setupSpinner();

        btnSave.setOnClickListener(v -> handleAddApartment());

        return view;
    }

    private void handleAddApartment() {
        // 1. Lấy dữ liệu từ EditText
        String room = edtRoomNumber.getText().toString().trim();
        String floor = edtFloor.getText().toString().trim();
        String area = edtArea.getText().toString().trim();
        String status = statusRaw[spinnerStatus.getSelectedItemPosition()];

        // 2. Kiểm tra rỗng (Validation)
        if(room.isEmpty() || floor.isEmpty() || area.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Tạo đối tượng Apartment để gửi đi
        Apartment newApt = new Apartment();
        newApt.setRoomNumber(room);
        newApt.setFloor(Integer.parseInt(floor));
        newApt.setArea(Double.parseDouble(area));
        newApt.setStatus(status);

        // 4. Gọi API POST
        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        apiService.createApartment(newApt).enqueue(new Callback<Apartment>() {
            @Override
            public void onResponse(Call<Apartment> call, Response<Apartment> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm căn hộ thành công!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                }
                else {
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
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_custom_toast, null);

        TextView text = layout.findViewById(R.id.tv_toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG); // Hiện lâu một chút cho người dùng kịp đọc
        toast.setView(layout);

        // Bạn có thể chỉnh vị trí hiện ở giữa màn hình nếu muốn
        toast.setGravity(Gravity.TOP, 0, 0);

        toast.show();
    }
}