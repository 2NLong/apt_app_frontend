package com.ptithcm.apt.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ptithcm.apt.R;

public class DialogUtils {

    private static AlertDialog loadingDialog;

    // Interface để nhận sự kiện khi người dùng bấm "Xác nhận"
    public interface ConfirmCallback {
        void onConfirm();
    }

    public static void showConfirmDialog(Context context,
                                         String title,
                                         String message,
                                         ConfirmCallback callback) {

        // 1. Khởi tạo View từ XML
        View view = LayoutInflater.from(context).inflate(R.layout.layout_confirm_dialog, null);

        // 2. Tạo AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .create();

        // Làm cho nền của Dialog trong suốt để thấy bo góc của CardView
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // 3. Ánh xạ các View trong XML và SET TEXT (Thay đổi tùy mục đích sử dụng)
        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        TextView tvMessage = view.findViewById(R.id.tvDialogMessage);
        Button btnConfirm = view.findViewById(R.id.btnDialogConfirm);
        Button btnCancel = view.findViewById(R.id.btnDialogCancel);

        tvTitle.setText(title);      // Gán tiêu đề truyền vào
        tvMessage.setText(message);  // Gán nội dung truyền vào

        // 4. Xử lý sự kiện nút bấm
        btnConfirm.setOnClickListener(v -> {
            if (callback != null) callback.onConfirm();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public static void showLoadingDialog(Context context) {
        if (loadingDialog != null && loadingDialog.isShowing()) return;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);

        loadingDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        if (loadingDialog.getWindow() != null) {
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        loadingDialog.show();
    }

    public static void showLoadingDialog(Context context, String message) {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            TextView tvMessage = loadingDialog.findViewById(R.id.tvLoadingMessage);
            if (tvMessage != null) tvMessage.setText(message);
            return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        TextView tvMessage = view.findViewById(R.id.tvLoadingMessage);
        if (tvMessage != null) tvMessage.setText(message);

        loadingDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        if (loadingDialog.getWindow() != null) {
            loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        loadingDialog.show();
    }

    public static void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
}