package com.ptithcm.apt.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.complaint.ComplaintResponse;
import com.ptithcm.apt.models.complaint.CreateComplaintRequest;
import com.ptithcm.apt.models.notification.NotificationResponse;
import com.ptithcm.apt.network.api.ComplaintApiService;
import com.ptithcm.apt.network.api.NotificationApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment {

    private LinearLayout panelNotifications;
    private LinearLayout panelComplaints;
    private LinearLayout notificationList;
    private LinearLayout complaintList;
    private TextView tabNotifications;
    private TextView tabComplaints;
    private TextView unreadCount;
    private EditText complaintTitleInput;
    private EditText complaintContentInput;
    private Spinner complaintCategorySpinner;
    private NotificationApiService notificationApiService;
    private ComplaintApiService complaintApiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews(view);
        initApiServices();
        setupCategorySpinner();
        setupTabs();
        setupActions();
        loadMyNotifications();
        loadMyComplaints();
    }

    private void bindViews(View view) {
        panelNotifications = view.findViewById(R.id.panel_notifications);
        panelComplaints = view.findViewById(R.id.panel_complaints);
        notificationList = view.findViewById(R.id.list_notifications);
        complaintList = view.findViewById(R.id.list_complaints);
        tabNotifications = view.findViewById(R.id.tab_notifications);
        tabComplaints = view.findViewById(R.id.tab_complaints);
        unreadCount = view.findViewById(R.id.txt_unread_count);
        complaintTitleInput = view.findViewById(R.id.input_complaint_title);
        complaintContentInput = view.findViewById(R.id.input_complaint_content);
        complaintCategorySpinner = view.findViewById(R.id.spinner_complaint_category);
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Sửa chữa", "Tiếng ồn", "Dịch vụ", "Khác"}
        );
        complaintCategorySpinner.setAdapter(adapter);
    }

    private void initApiServices() {
        notificationApiService = RetrofitClient.getInstance().createService(NotificationApiService.class);
        complaintApiService = RetrofitClient.getInstance().createService(ComplaintApiService.class);
    }

    private void setupTabs() {
        tabNotifications.setOnClickListener(v -> showNotifications());
        tabComplaints.setOnClickListener(v -> showComplaints());
    }

    private void setupActions() {
        Button markReadButton = requireView().findViewById(R.id.btn_mark_read);
        markReadButton.setOnClickListener(v -> {
            notificationApiService.markMyNotificationsAsRead().enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    unreadCount.setText("0");
                    loadMyNotifications();
                    Toast.makeText(requireContext(), "Đã đánh dấu tất cả thông báo là đã đọc", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    Toast.makeText(requireContext(), "Không thể cập nhật trạng thái đọc", Toast.LENGTH_SHORT).show();
                }
            });
        });

        Button submitButton = requireView().findViewById(R.id.btn_submit_complaint);
        submitButton.setOnClickListener(v -> submitComplaint());
    }

    private void showNotifications() {
        panelNotifications.setVisibility(View.VISIBLE);
        panelComplaints.setVisibility(View.GONE);
        tabNotifications.setBackgroundResource(R.drawable.bg_tab_active);
        tabNotifications.setTextColor(getResources().getColor(R.color.white, null));
        tabComplaints.setBackgroundResource(R.drawable.bg_tab_inactive);
        tabComplaints.setTextColor(getResources().getColor(R.color.text_body, null));
    }

    private void showComplaints() {
        panelNotifications.setVisibility(View.GONE);
        panelComplaints.setVisibility(View.VISIBLE);
        tabComplaints.setBackgroundResource(R.drawable.bg_tab_active);
        tabComplaints.setTextColor(getResources().getColor(R.color.white, null));
        tabNotifications.setBackgroundResource(R.drawable.bg_tab_inactive);
        tabNotifications.setTextColor(getResources().getColor(R.color.text_body, null));
    }

    private void loadMyNotifications() {
        notificationList.removeAllViews();
        notificationApiService.getMyNotifications().enqueue(new Callback<ApiResponse<List<NotificationResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NotificationResponse>>> call,
                                   Response<ApiResponse<List<NotificationResponse>>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    addInfoCard(notificationList, "Không tải được thông báo", "Vui lòng thử lại sau.", "", "");
                    return;
                }

                List<NotificationResponse> notifications = response.body().getData();
                long unread = notifications.stream().filter(item -> Boolean.FALSE.equals(item.getIsRead())).count();
                unreadCount.setText(String.valueOf(unread));

                if (notifications.isEmpty()) {
                    addInfoCard(notificationList, "Chưa có thông báo", "Ban quản lý chưa gửi thông báo nào cho căn hộ của bạn.", "", "");
                    return;
                }

                for (NotificationResponse item : notifications) {
                    MaterialCardView card = addInfoCard(
                            notificationList,
                            item.getTitle(),
                            item.getContent(),
                            formatDate(item.getCreatedAt()),
                            Boolean.TRUE.equals(item.getIsRead()) ? "Đã đọc" : "Chưa đọc"
                    );
                    card.setOnClickListener(v -> markNotificationReadAndShow(item));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NotificationResponse>>> call, Throwable t) {
                addInfoCard(notificationList, "Không kết nối được máy chủ", t.getLocalizedMessage(), "", "");
            }
        });
    }

    private void loadMyComplaints() {
        complaintList.removeAllViews();
        complaintApiService.getMyComplaints().enqueue(new Callback<ApiResponse<List<ComplaintResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ComplaintResponse>>> call,
                                   Response<ApiResponse<List<ComplaintResponse>>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    addInfoCard(complaintList, "Không tải được khiếu nại", "Vui lòng thử lại sau.", "", "");
                    return;
                }

                List<ComplaintResponse> complaints = response.body().getData();
                if (complaints.isEmpty()) {
                    addInfoCard(complaintList, "Chưa có khiếu nại", "Các khiếu nại bạn gửi sẽ hiển thị tại đây.", "", "");
                    return;
                }

                for (ComplaintResponse item : complaints) {
                    MaterialCardView card = addInfoCard(
                            complaintList,
                            item.getTitle(),
                            translateCategory(item.getCategory()) + " - " + item.getContent(),
                            formatDate(item.getCreatedAt()),
                            translateStatus(item.getStatus())
                    );
                    card.setOnClickListener(v -> showComplaintDetail(item));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ComplaintResponse>>> call, Throwable t) {
                addInfoCard(complaintList, "Không kết nối được máy chủ", t.getLocalizedMessage(), "", "");
            }
        });
    }

    private void submitComplaint() {
        String title = complaintTitleInput.getText().toString().trim();
        String content = complaintContentInput.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ tiêu đề và nội dung", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = mapCategory(complaintCategorySpinner.getSelectedItem().toString());
        CreateComplaintRequest request = new CreateComplaintRequest(null, category, title, content);
        complaintApiService.createComplaint(request).enqueue(new Callback<ApiResponse<ComplaintResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ComplaintResponse>> call,
                                   Response<ApiResponse<ComplaintResponse>> response) {
                if (response.isSuccessful()) {
                    complaintTitleInput.setText("");
                    complaintContentInput.setText("");
                    loadMyComplaints();
                    Toast.makeText(requireContext(), "Đã gửi khiếu nại", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Gửi khiếu nại thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ComplaintResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không kết nối được máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MaterialCardView addInfoCard(LinearLayout parent, String title, String content, String time, String status) {
        MaterialCardView card = new MaterialCardView(requireContext());
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dp(12));
        card.setLayoutParams(cardParams);
        card.setCardElevation(0);
        card.setRadius(dp(8));
        card.setCardBackgroundColor(getResources().getColor(R.color.white, null));

        LinearLayout body = new LinearLayout(requireContext());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setPadding(dp(16), dp(14), dp(16), dp(14));

        LinearLayout header = new LinearLayout(requireContext());
        header.setOrientation(LinearLayout.HORIZONTAL);
        header.setGravity(android.view.Gravity.CENTER_VERTICAL);

        TextView titleView = new TextView(requireContext());
        titleView.setText(title);
        titleView.setTextColor(getResources().getColor(R.color.text_title, null));
        titleView.setTextSize(16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);
        header.addView(titleView, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        TextView statusView = new TextView(requireContext());
        statusView.setText(status);
        statusView.setTextColor(getStatusColor(status));
        statusView.setTextSize(12);
        statusView.setTypeface(null, android.graphics.Typeface.BOLD);
        header.addView(statusView);

        TextView contentView = new TextView(requireContext());
        contentView.setText(content);
        contentView.setTextColor(getResources().getColor(R.color.text_body, null));
        contentView.setTextSize(14);
        contentView.setPadding(0, dp(8), 0, 0);

        TextView timeView = new TextView(requireContext());
        timeView.setText(time);
        timeView.setTextColor(getResources().getColor(R.color.text_secondary, null));
        timeView.setTextSize(12);
        timeView.setPadding(0, dp(8), 0, 0);

        body.addView(header);
        body.addView(contentView);
        body.addView(timeView);
        card.addView(body);
        parent.addView(card, 0);
        return card;
    }

    private void showNotificationDetail(NotificationResponse item) {
        showNotificationDetail(item, Boolean.TRUE.equals(item.getIsRead()));
    }

    private void showNotificationDetail(NotificationResponse item, boolean isRead) {
        String message = "Tiêu đề: " + safe(item.getTitle())
                + "\n\nNội dung:\n" + safe(item.getContent())
                + "\n\nGửi tới: " + getNotificationTargetText(item)
                + "\nNgày gửi: " + formatDate(item.getCreatedAt())
                + "\nTrạng thái: " + (isRead ? "Đã đọc" : "Chưa đọc");

        new AlertDialog.Builder(requireContext())
                .setTitle("Chi tiết thông báo")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void markNotificationReadAndShow(NotificationResponse item) {
        if (Boolean.TRUE.equals(item.getIsRead())) {
            showNotificationDetail(item, true);
            return;
        }

        notificationApiService.markMyNotificationAsRead(item.getId()).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    item.setIsRead(true);
                    showNotificationDetail(item, true);
                    loadMyNotifications();
                } else {
                    showNotificationDetail(item, false);
                    Toast.makeText(requireContext(), "Không thể cập nhật trạng thái đọc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                showNotificationDetail(item, false);
                Toast.makeText(requireContext(), "Không thể cập nhật trạng thái đọc", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showComplaintDetail(ComplaintResponse item) {
        String message = "Tiêu đề: " + safe(item.getTitle())
                + "\n\nNội dung:\n" + safe(item.getContent())
                + "\n\nCăn hộ: " + safe(item.getRoomNumber())
                + "\nLoại: " + translateCategory(item.getCategory())
                + "\nTrạng thái: " + translateStatus(item.getStatus())
                + "\nNgày gửi: " + formatDate(item.getCreatedAt());

        new AlertDialog.Builder(requireContext())
                .setTitle("Chi tiết khiếu nại")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private String getNotificationTargetText(NotificationResponse item) {
        if (item.getTargetSummary() != null && !item.getTargetSummary().trim().isEmpty()) {
            return item.getTargetSummary();
        }
        return "Căn hộ của bạn";
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "Không có dữ liệu" : value;
    }

    private int getStatusColor(String status) {
        if (status.contains("Chưa") || status.contains("Đang")) {
            return getResources().getColor(R.color.primary, null);
        }
        if (status.contains("tiếp nhận")) {
            return getResources().getColor(R.color.indicator_current, null);
        }
        return getResources().getColor(R.color.text_secondary, null);
    }

    private String mapCategory(String label) {
        switch (label) {
            case "Sửa chữa":
                return "REPAIR";
            case "Tiếng ồn":
                return "NOISE";
            case "Dịch vụ":
                return "SERVICE";
            default:
                return "OTHER";
        }
    }

    private String translateCategory(String category) {
        if ("REPAIR".equals(category)) return "Sửa chữa";
        if ("NOISE".equals(category)) return "Tiếng ồn";
        if ("SERVICE".equals(category)) return "Dịch vụ";
        return "Khác";
    }

    private String translateStatus(String status) {
        if ("DONE".equals(status)) return "Hoàn thành";
        return "Đã tiếp nhận";
    }

    private String formatDate(String value) {
        if (value == null || value.length() < 10) {
            return "";
        }
        return value.substring(0, 10);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
