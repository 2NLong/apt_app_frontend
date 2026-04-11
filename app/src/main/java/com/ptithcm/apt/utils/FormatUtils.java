package com.ptithcm.apt.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class FormatUtils {

    /**
     * Định dạng tiền tệ theo chuẩn Việt Nam (VD: 5.000.000 đ)
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) return "---";
        try {
            NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
            return formatter.format(amount);
        } catch (Exception e) {
            return amount.toString() + " VNĐ";
        }
    }

    /**
     * Chuyển đổi ngày tháng từ chuẩn ISO (yyyy-MM-dd) sang dạng Việt Nam (dd/MM/yyyy)
     */
    public static String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return dateStr;
        if (dateStr.contains("T")) {
            dateStr = dateStr.split("T")[0];
        }
        String[] parts = dateStr.split("-");
        if (parts.length == 3) {
            return parts[2] + "/" + parts[1] + "/" + parts[0];
        }
        return dateStr;
    }
}
