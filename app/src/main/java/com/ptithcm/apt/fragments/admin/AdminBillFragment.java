package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.ptithcm.apt.R;

public class AdminBillFragment extends Fragment {

    private int currentSelectedMonth = 4;
    private int currentSelectedYear = 2026;

    public AdminBillFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Nạp layout fragment_admin_bill
        return inflater.inflate(R.layout.fragment_admin_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Tìm ChipDateFilter sau khi View đã được tạo thành công
        Chip chipDate = view.findViewById(R.id.chipDateFilter);

        if (chipDate != null) {
            chipDate.setOnClickListener(v -> showMonthYearPicker());
        }
    }

    private void showMonthYearPicker() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_month_year_picker, null);

        NumberPicker pickerMonth = dialogView.findViewById(R.id.pickerMonth);
        NumberPicker pickerYear = dialogView.findViewById(R.id.pickerYear);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        if (pickerMonth != null) {
            pickerMonth.setMinValue(1);
            pickerMonth.setMaxValue(12);
            pickerMonth.setValue(currentSelectedMonth);
        }

        if (pickerYear != null) {
            pickerYear.setMinValue(2020);
            pickerYear.setMaxValue(2030);
            pickerYear.setValue(currentSelectedYear);
        }

        if (btnConfirm != null) {
            btnConfirm.setOnClickListener(v -> {
                if (pickerMonth != null && pickerYear != null) {
                    currentSelectedMonth = pickerMonth.getValue();
                    currentSelectedYear = pickerYear.getValue();

                    // Cập nhật lại text cho Chip
                    View view = getView();
                    if (view != null) {
                        Chip chipDate = view.findViewById(R.id.chipDateFilter);
                        if (chipDate != null) {
                            chipDate.setText("Tháng " + currentSelectedMonth + "/" + currentSelectedYear);
                        }
                    }

                    // TODO: Gọi hàm reload dữ liệu hóa đơn tại đây
                }
                dialog.dismiss();
            });
        }

        dialog.setContentView(dialogView);
        dialog.show();
    }
}
