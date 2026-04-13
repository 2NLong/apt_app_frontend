package com.ptithcm.apt.fragments.apartment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.ManagerApartmentAdapter;
import com.ptithcm.apt.models.apartment.Apartment;
import com.ptithcm.apt.models.apartment.ApartmentPageResponse;
import com.ptithcm.apt.network.api.ApartmentApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManageApartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageApartmentFragment extends Fragment {

    private RecyclerView rvApartments;
    private EditText edtSearch;
    private Spinner spinnerFilter;
    private Button btnPrev, btnNext;
    private TextView tvPageInfo;

    FloatingActionButton fabAddApartment;

    private ManagerApartmentAdapter adapter;

    private LinearLayout layoutPagination;
    private String[] filterDisplay = {"Tất cả các phòng", "Phòng trống", "Đang cho thuê", "Có chủ sở hữu"};
    private String[] filterRaw = {"ALL", "AVAILABLE", "RENTED", "OWNED"};

    // Biến phân trang cho Server (Trang đầu tiên của Spring Boot là 0)
    private int currentPage = 0;
    private int totalPages = 1;

    // Handler dùng cho tính năng chống spam tìm kiếm (Debounce)
    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ManageApartmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageApartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageApartmentFragment newInstance(String param1, String param2) {
        ManageApartmentFragment fragment = new ManageApartmentFragment();
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
        // Nạp giao diện vào biến 'view'
        View view = inflater.inflate(R.layout.fragment_manage_apartment, container, false);

        rvApartments = view.findViewById(R.id.rv_apartments);
        edtSearch = view.findViewById(R.id.edt_search);

        spinnerFilter = view.findViewById(R.id.spinner_filter_status);
        layoutPagination = view.findViewById(R.id.layout_pagination);

        btnPrev = view.findViewById(R.id.btn_prev_page);
        btnNext = view.findViewById(R.id.btn_next_page);
        tvPageInfo = view.findViewById(R.id.tv_page_info);
        fabAddApartment = view.findViewById(R.id.fab_add_apartment);
        fabAddApartment.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, new AddApartmentFragment())
                    .addToBackStack(null)
                    .commit();
        });

        rvApartments.setLayoutManager(new LinearLayoutManager(getContext()));
        // Khởi tạo Adapter và bắt sự kiện click ngay tại đây
        adapter = new ManagerApartmentAdapter(new ArrayList<>(), apartment -> {

            // Nhờ có khai báo "apartment ->" ở dòng trên, bây giờ máy đã hiểu "apartment" là gì!
            Bundle bundle = new Bundle();
            bundle.putLong("APARTMENT_ID", apartment.getId());

            UpdateApartmentFragment updateFragment = new UpdateApartmentFragment();
            updateFragment.setArguments(bundle);

            // Chuyển trang
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.admin_fragment_container, updateFragment)
                    .addToBackStack(null)
                    .commit();
        });

        // Nhớ set lại adapter cho RecyclerView
        rvApartments.setAdapter(adapter);
        rvApartments.setAdapter(adapter);

        setupPaginationListeners();
        setupSearchListener();
        setupFilterSpinner();

        // Gọi trang đầu tiên (index 0)
        fetchApartmentsByPage(0);

        return view;
    }
    private void fetchApartmentsByPage(int page) {
        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);

        apiService.getApartmentsByPage(page).enqueue(new Callback<ApartmentPageResponse>() {
            @Override
            public void onResponse(Call<ApartmentPageResponse> call, Response<ApartmentPageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApartmentPageResponse pageResponse = response.body();

                    // Lấy mảng dữ liệu thực tế
                    adapter.updateData(pageResponse.getContent());

                    // Cập nhật thông số trang
                    totalPages = pageResponse.getTotalPages();
                    currentPage = pageResponse.getNumber(); // Backend trả về trang mấy thì gán vào

                    // Hiển thị ra màn hình (Cộng 1 vì User đếm từ 1, Backend đếm từ 0)
                    tvPageInfo.setText((currentPage + 1) + " / " + Math.max(1, totalPages));

                    // Cập nhật trạng thái nút
                    btnPrev.setEnabled(currentPage > 0);
                    btnNext.setEnabled(currentPage < totalPages - 1);
                }
            }

            @Override
            public void onFailure(Call<ApartmentPageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPaginationListeners() {
        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) fetchApartmentsByPage(currentPage - 1);
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) fetchApartmentsByPage(currentPage + 1);
        });
    }

    // --- GỌI API TÌM KIẾM ---
    private void setupSearchListener() {
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString().trim();

                // Hủy lệnh tìm kiếm cũ nếu người dùng vẫn đang gõ liên tục
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                // Cài đặt lệnh gọi API mới
                searchRunnable = () -> {
                    if (keyword.isEmpty()) {
                        fetchApartmentsByPage(0); // Nếu xóa hết từ khóa, quay lại xem trang 0
                    } else {
                        performApiSearch(keyword); // Gọi API tìm kiếm
                    }
                };

                // Đợi 500ms sau khi ngừng gõ mới thực thi (Chống sập Server)
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void performApiSearch(String keyword) {
        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);

        apiService.searchApartments(keyword).enqueue(new Callback<List<Apartment>>() {
            @Override
            public void onResponse(Call<List<Apartment>> call, Response<List<Apartment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Cập nhật danh sách từ kết quả tìm kiếm
                    adapter.updateData(response.body());

                    // Khi tìm kiếm, hiển thị số trang là 1/1 và khóa 2 nút phân trang lại
                    tvPageInfo.setText("1 / 1");
                    btnPrev.setEnabled(false);
                    btnNext.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<List<Apartment>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tìm kiếm: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupFilterSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, filterDisplay);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(adapter);

        // Bắt sự kiện khi người dùng chọn 1 dòng
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = filterRaw[position];

                if (selectedStatus.equals("ALL")) {
                    // Nếu chọn "Tất cả", hiện lại thanh phân trang và load trang 0
                    layoutPagination.setVisibility(View.VISIBLE);
                    fetchApartmentsByPage(0); // Gọi lại hàm lấy Page cũ của bạn
                } else {
                    // Nếu chọn trạng thái cụ thể, ẩn thanh phân trang và gọi API lọc
                    layoutPagination.setVisibility(View.GONE);
                    fetchApartmentsByStatus(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void fetchApartmentsByStatus(String status) {
        ApartmentApiService apiService = RetrofitClient.getInstance().createService(ApartmentApiService.class);
        apiService.getApartmentsByStatus(status).enqueue(new Callback<List<Apartment>>() {
            @Override
            public void onResponse(Call<List<Apartment>> call, Response<List<Apartment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Apartment> list = response.body();

                    // Cập nhật lại adapter của RecyclerView
                    // Giả sử adapter của bạn có hàm updateData(List)
                    adapter.updateData(list);

                    if (list.isEmpty()) {
                        Toast.makeText(getContext(), "Không có phòng nào ở trạng thái này!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Apartment>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}