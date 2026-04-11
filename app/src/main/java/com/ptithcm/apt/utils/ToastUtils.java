package com.ptithcm.apt.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ptithcm.apt.R;

public class ToastUtils {

    /**
     * Hiển thị thông báo thành công (Màu Xanh lá cây)
     */
    public static void showSuccessToast(Context context, String message) {
        if (context == null)
            return;

        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.layout_toast_success, null);

            TextView text = layout.findViewById(R.id.tv_toast_message_success);
            if (text != null) {
                text.setText(message);
            }

            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);

            toast.setGravity(Gravity.TOP | Gravity.START, 36, 36);
            toast.show();
        } catch (Exception e) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Hiển thị thông báo lỗi (Màu Đỏ)
     */
    public static void showErrorToast(Context context, String message) {
        if (context == null)
            return;

        try {
            LayoutInflater inflater = LayoutInflater.from(context);
            View layout = inflater.inflate(R.layout.layout_toast_error, null);

            TextView text = layout.findViewById(R.id.tv_toast_message_error);
            if (text != null) {
                text.setText(message);
            }

            Toast toast = new Toast(context);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);

            toast.setGravity(Gravity.TOP | Gravity.START, 36, 36);
            toast.show();
        } catch (Exception e) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
