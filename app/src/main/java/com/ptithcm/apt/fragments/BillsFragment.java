package com.ptithcm.apt.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ptithcm.apt.R;
import com.ptithcm.apt.adapters.BillAdapter;
import com.ptithcm.apt.models.Bill;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Color;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView rvBills;
    private BillAdapter adapter;

    private List<Bill> unpaidList = new ArrayList<>();
    private List<Bill> paidList = new ArrayList<>();

    private TextView tabUnpaid, tabHistory;
    private TextView tvDebt, tvPaid;

    public BillsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BillsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BillsFragment newInstance(String param1, String param2) {
        BillsFragment fragment = new BillsFragment();
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills, container, false);

        // INIT VIEW
        rvBills = view.findViewById(R.id.rvBills);
        tabUnpaid = view.findViewById(R.id.tabUnpaid);
        tabHistory = view.findViewById(R.id.tabHistory);
        tvDebt = view.findViewById(R.id.tvDebt);
        tvPaid = view.findViewById(R.id.tvPaid);

        rvBills.setLayoutManager(new LinearLayoutManager(getContext()));

        // DATA FAKE (sau này replace bằng API)
        initData();

        adapter = new BillAdapter(unpaidList);
        rvBills.setAdapter(adapter);

        // SET SUMMARY

        // TAB CLICK
        tabUnpaid.setOnClickListener(v -> {
            adapter.updateList(unpaidList);
            // Sửa tại đây
            tabUnpaid.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            tabHistory.setTextColor(ContextCompat.getColor(getContext(), R.color.text_title));
        });

        tabHistory.setOnClickListener(v -> {
            adapter.updateList(paidList);
            // Sửa tại đây
            tabHistory.setTextColor(ContextCompat.getColor(getContext(), R.color.primary));
            tabUnpaid.setTextColor(ContextCompat.getColor(getContext(), R.color.text_title));
        });

        return view;
    }



    private void initData() {
        unpaidList.add(new Bill(101, "2023-10-01", 1, 850000.0, 200000.0, 10, null, 50000.0, 350000.0, "UNPAID", 1450000.0, 2023)); unpaidList.add(new Bill(102, "2023-10-02", 2, 920000.0, 200000.0, 10, null, 50000.0, 410000.0, "UNPAID", 1580000.0, 2023)); unpaidList.add(new Bill(103, "2023-10-03", 3, 500000.0, 200000.0, 10, null, 50000.0, 150000.0, "UNPAID", 900000.0, 2023));
        unpaidList.add(new Bill(101, "2023-10-01", 1, 850000.0, 200000.0, 10, null, 50000.0, 350000.0, "UNPAID", 1450000.0, 2023)); unpaidList.add(new Bill(102, "2023-10-02", 2, 920000.0, 200000.0, 10, null, 50000.0, 410000.0, "UNPAID", 1580000.0, 2023)); unpaidList.add(new Bill(103, "2023-10-03", 3, 500000.0, 200000.0, 10, null, 50000.0, 150000.0, "UNPAID", 900000.0, 2023));
        unpaidList.add(new Bill(101, "2023-10-01", 1, 850000.0, 200000.0, 10, null, 50000.0, 350000.0, "UNPAID", 1450000.0, 2023)); unpaidList.add(new Bill(102, "2023-10-02", 2, 920000.0, 200000.0, 10, null, 50000.0, 410000.0, "UNPAID", 1580000.0, 2023)); unpaidList.add(new Bill(103, "2023-10-03", 3, 500000.0, 200000.0, 10, null, 50000.0, 150000.0, "UNPAID", 900000.0, 2023));

        paidList.add(new Bill(101, "2023-09-01", 4, 750000.0, 200000.0, 9, "2023-09-05", 50000.0, 300000.0, "PAID", 1300000.0, 2023)); paidList.add(new Bill(104, "2023-09-01", 5, 600000.0, 200000.0, 9, "2023-09-07", 50000.0, 250000.0, "PAID", 1100000.0, 2023)); paidList.add(new Bill(105, "2023-08-01", 6, 880000.0, 200000.0, 8, "2023-08-10", 50000.0, 320000.0, "PAID", 1450000.0, 2023));
        paidList.add(new Bill(101, "2023-09-01", 4, 750000.0, 200000.0, 9, "2023-09-05", 50000.0, 300000.0, "PAID", 1300000.0, 2023)); paidList.add(new Bill(104, "2023-09-01", 5, 600000.0, 200000.0, 9, "2023-09-07", 50000.0, 250000.0, "PAID", 1100000.0, 2023)); paidList.add(new Bill(105, "2023-08-01", 6, 880000.0, 200000.0, 8, "2023-08-10", 50000.0, 320000.0, "PAID", 1450000.0, 2023));
        paidList.add(new Bill(101, "2023-09-01", 4, 750000.0, 200000.0, 9, "2023-09-05", 50000.0, 300000.0, "PAID", 1300000.0, 2023)); paidList.add(new Bill(104, "2023-09-01", 5, 600000.0, 200000.0, 9, "2023-09-07", 50000.0, 250000.0, "PAID", 1100000.0, 2023)); paidList.add(new Bill(105, "2023-08-01", 6, 880000.0, 200000.0, 8, "2023-08-10", 50000.0, 320000.0, "PAID", 1450000.0, 2023));
    }
}