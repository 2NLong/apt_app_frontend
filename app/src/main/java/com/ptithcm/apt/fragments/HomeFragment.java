package com.ptithcm.apt.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.serviceconfig.HomeServiceConfigAdapter;
import com.ptithcm.apt.network.api.ServiceConfigApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;
import com.ptithcm.apt.repositoris.ServiceConfigRepository;
import com.ptithcm.apt.viewmodel.home.HomeViewModel;
import com.ptithcm.apt.viewmodel.home.HomeViewModelFactory;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private HomeViewModel viewModel;
    private HomeServiceConfigAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView errorText;
    
    private ImageView btnPrevMonth, btnNextMonth;
    private TextView textSelectedMonth;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupDateControls(view);
        setupViewModel();
        setupObserver();

        // Initial fetch
        viewModel.refreshData();
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_home_service_configs);
        progressBar = view.findViewById(R.id.loading_progress);
        errorText = view.findViewById(R.id.text_error);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HomeServiceConfigAdapter(requireContext());
        recyclerView.setAdapter(adapter);
    }

    private void setupDateControls(View view) {
        btnPrevMonth = view.findViewById(R.id.btn_prev_month);
        btnNextMonth = view.findViewById(R.id.btn_next_month);
        textSelectedMonth = view.findViewById(R.id.text_selected_month);

        btnPrevMonth.setOnClickListener(v -> viewModel.previousMonth());
        btnNextMonth.setOnClickListener(v -> viewModel.nextMonth());
    }

    private void setupViewModel() {
        ServiceConfigApiService apiService = RetrofitClient.getInstance().createService(ServiceConfigApiService.class);
        ServiceConfigRepository repository = new ServiceConfigRepository(apiService);
        HomeViewModelFactory factory = new HomeViewModelFactory(repository);
        viewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    private void setupObserver() {
        viewModel.activeServiceConfigs.observe(getViewLifecycleOwner(), configs -> {
            if (configs != null && !configs.isEmpty()) {
                adapter.submitList(configs);
                errorText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else if (configs != null) {
                errorText.setText("Không có dữ liệu biểu phí cho tháng này.");
                errorText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                errorText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        viewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                errorText.setText(error);
                errorText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });

        viewModel.selectedDate.observe(getViewLifecycleOwner(), date -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM / yyyy", Locale.getDefault());
            textSelectedMonth.setText(date.format(formatter));
        });
    }
}