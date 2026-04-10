package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ptithcm.apt.R;
import com.ptithcm.apt.fragments.apartment.ManageApartmentFragment;
import com.ptithcm.apt.fragments.ManageContractFragment;
import com.ptithcm.apt.fragments.ManageResidentFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminHomeFragment extends Fragment {

//    private Button btnManageApartment, btnManageResident, btnContract;
    private androidx.cardview.widget.CardView btnManageApartment, btnManageResident, btnContract;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminHomeFragment newInstance(String param1, String param2) {
        AdminHomeFragment fragment = new AdminHomeFragment();
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
        View view  = inflater.inflate(R.layout.fragment_admin_home,container,false);

        btnManageApartment = view.findViewById(R.id.btn_manage_apartment);
        btnManageResident = view.findViewById(R.id.btn_manage_resident);
        btnContract = view.findViewById(R.id.btn_contract);

        btnManageApartment.setOnClickListener(v -> openFragment(new ManageApartmentFragment()));
        btnManageResident.setOnClickListener(v -> openFragment(new ManageResidentFragment()));
        btnContract.setOnClickListener(v -> openFragment(new ManageContractFragment()));
        return view;
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        transaction.add(R.id.admin_fragment_container, fragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }
}