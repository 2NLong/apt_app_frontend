package com.ptithcm.apt.fragments.admin;

import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.ptithcm.apt.R;
import com.ptithcm.apt.models.auth.response.ApiResponse;
import com.ptithcm.apt.models.complaint.ComplaintResponse;
import com.ptithcm.apt.models.complaint.UpdateComplaintStatusRequest;
import com.ptithcm.apt.models.notification.CreateNotificationRequest;
import com.ptithcm.apt.models.notification.NotificationResponse;
import com.ptithcm.apt.models.notification.NotificationTargetResponse;
import com.ptithcm.apt.network.api.ComplaintApiService;
import com.ptithcm.apt.network.api.NotificationApiService;
import com.ptithcm.apt.network.retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminNotificationFragment extends Fragment {

    private LinearLayout adminComplaintList;
    private LinearLayout adminNotificationList;
    private Spinner targetSpinner;
    private Spinner notificationApartmentFilter;
    private TextView recipientPreview;
    private EditText notificationDateFilter;
    private EditText titleInput;
    private EditText contentInput;
    private NotificationApiService notificationApiService;
    private ComplaintApiService complaintApiService;
    private final List<NotificationTargetResponse> notificationTargets = new ArrayList<>();
    private final List<NotificationResponse> adminNotifications = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adminComplaintList = view.findViewById(R.id.list_admin_complaints);
        adminNotificationList = view.findViewById(R.id.list_admin_notifications);
        targetSpinner = view.findViewById(R.id.spinner_notification_target);
        notificationApartmentFilter = view.findViewById(R.id.spinner_notification_apartment_filter);
        recipientPreview = view.findViewById(R.id.text_notification_recipient_preview);
        notificationDateFilter = view.findViewById(R.id.input_notification_date_filter);
        titleInput = view.findViewById(R.id.input_notification_title);
        contentInput = view.findViewById(R.id.input_notification_content);
        notificationApiService = RetrofitClient.getInstance().createService(NotificationApiService.class);
        complaintApiService = RetrofitClient.getInstance().createService(ComplaintApiService.class);

        setupTargetSpinner();
        setupNotificationFilters();
        loadComplaints();
        loadNotifications();

        Button sendButton = view.findViewById(R.id.btn_send_notification);
        sendButton.setOnClickListener(v -> sendNotification());
    }

    private void setupTargetSpinner() {
        notificationTargets.clear();
        setTargetSpinnerLabels(Collections.singletonList("Đang tải danh sách chủ hộ..."));
        targetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateRecipientPreview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                updateRecipientPreview();
            }
        });
        loadNotificationTargets();
    }

    private void setupNotificationFilters() {
        setNotificationFilterLabels(Collections.singletonList("Tất cả căn hộ"));
        notificationApartmentFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                renderAdminNotifications();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                renderAdminNotifications();
            }
        });

        notificationDateFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderAdminNotifications();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void loadNotificationTargets() {
        notificationApiService.getNotificationTargets().enqueue(new Callback<ApiResponse<List<NotificationTargetResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NotificationTargetResponse>>> call,
                                   Response<ApiResponse<List<NotificationTargetResponse>>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    renderTargetSpinner();
                    Toast.makeText(requireContext(), "Không tải được danh sách chủ hộ", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (NotificationTargetResponse target : response.body().getData()) {
                    if (!hasNotificationTarget(target.getApartmentId())) {
                        notificationTargets.add(target);
                    }
                }
                renderTargetSpinner();
                renderNotificationFilterSpinner();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NotificationTargetResponse>>> call, Throwable t) {
                renderTargetSpinner();
                Toast.makeText(requireContext(), "Không kết nối được máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderTargetSpinner() {
        List<String> labels = new ArrayList<>();
        labels.add("Tất cả chủ hộ đang hoạt động");
        for (NotificationTargetResponse target : notificationTargets) {
            String roomNumber = target.getRoomNumber() != null ? target.getRoomNumber() : String.valueOf(target.getApartmentId());
            labels.add("Căn " + roomNumber);
        }
        setTargetSpinnerLabels(labels);
        updateRecipientPreview();
    }

    private void renderNotificationFilterSpinner() {
        List<String> labels = new ArrayList<>();
        labels.add("Tất cả căn hộ");
        for (NotificationTargetResponse target : notificationTargets) {
            String roomNumber = target.getRoomNumber() != null ? target.getRoomNumber() : String.valueOf(target.getApartmentId());
            labels.add("Căn " + roomNumber);
        }
        setNotificationFilterLabels(labels);
    }

    private void setTargetSpinnerLabels(List<String> labels) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                labels
        );
        targetSpinner.setAdapter(adapter);
    }

    private void setNotificationFilterLabels(List<String> labels) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                labels
        );
        notificationApartmentFilter.setAdapter(adapter);
    }

    private boolean hasNotificationTarget(Long apartmentId) {
        for (NotificationTargetResponse target : notificationTargets) {
            if (target.getApartmentId() != null && target.getApartmentId().equals(apartmentId)) {
                return true;
            }
        }
        return false;
    }

    private void loadComplaints() {
        adminComplaintList.removeAllViews();
        complaintApiService.getAllComplaints().enqueue(new Callback<ApiResponse<List<ComplaintResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ComplaintResponse>>> call,
                                   Response<ApiResponse<List<ComplaintResponse>>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    addInfoCard(adminComplaintList, "Không tải được khiếu nại", "Vui lòng thử lại sau.", "", "");
                    return;
                }

                List<ComplaintResponse> complaints = response.body().getData();
                if (complaints.isEmpty()) {
                    addInfoCard(adminComplaintList, "Chưa có khiếu nại", "Khi cư dân gửi phản ánh, danh sách sẽ hiển thị tại đây.", "", "");
                    return;
                }

                for (ComplaintResponse item : complaints) {
                    addComplaintCard(item);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<ComplaintResponse>>> call, Throwable t) {
                addInfoCard(adminComplaintList, "Không kết nối được máy chủ", t.getLocalizedMessage(), "", "");
            }
        });
    }

    private void loadNotifications() {
        notificationApiService.getAllNotifications().enqueue(new Callback<ApiResponse<List<NotificationResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<NotificationResponse>>> call,
                                   Response<ApiResponse<List<NotificationResponse>>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getData() == null) {
                    adminNotificationList.removeAllViews();
                    addInfoCard(adminNotificationList, "Không tải được thông báo", "Vui lòng thử lại sau.", "", "");
                    return;
                }

                adminNotifications.clear();
                adminNotifications.addAll(response.body().getData());
                renderAdminNotifications();
            }

            @Override
            public void onFailure(Call<ApiResponse<List<NotificationResponse>>> call, Throwable t) {
                adminNotificationList.removeAllViews();
                addInfoCard(adminNotificationList, "Không kết nối được máy chủ", t.getLocalizedMessage(), "", "");
            }
        });
    }

    private void renderAdminNotifications() {
        if (adminNotificationList == null) {
            return;
        }

        adminNotificationList.removeAllViews();
        String dateFilter = notificationDateFilter.getText().toString().trim();
        String roomFilter = getSelectedFilterRoomNumber();
        int renderedCount = 0;

        for (NotificationResponse item : adminNotifications) {
            if (!matchesDateFilter(item, dateFilter) || !matchesRoomFilter(item, roomFilter)) {
                continue;
            }

            MaterialCardView card = addInfoCard(adminNotificationList, item.getTitle(), item.getContent(), formatDate(item.getCreatedAt()), "Đã gửi");
            card.setOnClickListener(v -> showNotificationDetail(item));
            renderedCount++;
        }

        if (renderedCount == 0) {
            addInfoCard(adminNotificationList, "Không có thông báo phù hợp", "Thử đổi ngày hoặc căn hộ lọc.", "", "");
        }
    }

    private boolean matchesDateFilter(NotificationResponse item, String dateFilter) {
        if (dateFilter.isEmpty()) {
            return true;
        }
        String createdAt = item.getCreatedAt();
        return createdAt != null && createdAt.startsWith(dateFilter);
    }

    private boolean matchesRoomFilter(NotificationResponse item, String roomFilter) {
        if (roomFilter == null) {
            return true;
        }
        return item.getRoomNumbers() != null && item.getRoomNumbers().contains(roomFilter);
    }

    private String getSelectedFilterRoomNumber() {
        int selectedPosition = notificationApartmentFilter.getSelectedItemPosition();
        if (selectedPosition <= 0 || selectedPosition > notificationTargets.size()) {
            return null;
        }
        return notificationTargets.get(selectedPosition - 1).getRoomNumber();
    }

    private void sendNotification() {
        String title = titleInput.getText().toString().trim();
        String content = contentInput.getText().toString().trim();

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập tiêu đề và nội dung thông báo", Toast.LENGTH_SHORT).show();
            return;
        }

        String targetType = "ALL";
        List<Long> apartmentIds = null;
        int selectedPosition = targetSpinner.getSelectedItemPosition();
        if (selectedPosition > 0 && selectedPosition <= notificationTargets.size()) {
            targetType = "SPECIFIC";
            apartmentIds = Collections.singletonList(notificationTargets.get(selectedPosition - 1).getApartmentId());
        }

        CreateNotificationRequest request = new CreateNotificationRequest(title, content, targetType, apartmentIds);
        confirmSendNotification(request, getSelectedTargetLabel());
    }

    private void confirmSendNotification(CreateNotificationRequest request, String targetLabel) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận gửi thông báo")
                .setMessage("Gửi thông báo này tới " + targetLabel + "?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Gửi", (dialog, which) -> submitNotification(request))
                .show();
    }

    private void submitNotification(CreateNotificationRequest request) {
        notificationApiService.createNotification(request).enqueue(new Callback<ApiResponse<NotificationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<NotificationResponse>> call,
                                   Response<ApiResponse<NotificationResponse>> response) {
                if (response.isSuccessful()) {
                    titleInput.setText("");
                    contentInput.setText("");
                    loadNotifications();
                    Toast.makeText(requireContext(), "Đã gửi thông báo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Gửi thông báo thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<NotificationResponse>> call, Throwable t) {
                Toast.makeText(requireContext(), "Không kết nối được máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecipientPreview() {
        if (recipientPreview == null) {
            return;
        }

        int selectedPosition = targetSpinner.getSelectedItemPosition();
        if (selectedPosition <= 0 || selectedPosition > notificationTargets.size()) {
            recipientPreview.setText("Người nhận: Tất cả chủ hộ đang hoạt động (" + notificationTargets.size() + " người)");
            return;
        }

        NotificationTargetResponse target = notificationTargets.get(selectedPosition - 1);
        recipientPreview.setText("Người nhận: " + safe(target.getResidentName())
                + "\nCăn " + safe(target.getRoomNumber())
                + " · " + safe(target.getResidentEmail()));
    }

    private String getSelectedTargetLabel() {
        int selectedPosition = targetSpinner.getSelectedItemPosition();
        if (selectedPosition <= 0 || selectedPosition > notificationTargets.size()) {
            return "tất cả chủ hộ đang hoạt động";
        }

        NotificationTargetResponse target = notificationTargets.get(selectedPosition - 1);
        return "căn " + safe(target.getRoomNumber()) + " (" + safe(target.getResidentName()) + ")";
    }

    private void addComplaintCard(ComplaintResponse item) {
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

        TextView titleView = new TextView(requireContext());
        titleView.setText((item.getRoomNumber() != null ? item.getRoomNumber() + " - " : "") + item.getTitle());
        titleView.setTextColor(getResources().getColor(R.color.text_title, null));
        titleView.setTextSize(16);
        titleView.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView contentView = new TextView(requireContext());
        contentView.setText((item.getResidentName() != null ? item.getResidentName() + ": " : "") + item.getContent());
        contentView.setTextColor(getResources().getColor(R.color.text_body, null));
        contentView.setTextSize(14);
        contentView.setPadding(0, dp(8), 0, 0);

        TextView metaView = new TextView(requireContext());
        metaView.setText(translateCategory(item.getCategory()) + " · " + translateStatus(item.getStatus()) + " · " + formatDate(item.getCreatedAt()));
        metaView.setTextColor(getStatusColor(translateStatus(item.getStatus())));
        metaView.setTextSize(12);
        metaView.setTypeface(null, android.graphics.Typeface.BOLD);
        metaView.setPadding(0, dp(8), 0, 0);

        body.addView(titleView);
        body.addView(contentView);
        body.addView(metaView);

        if (!"DONE".equals(item.getStatus())) {
            Button doneButton = new Button(requireContext());
            doneButton.setText("Đánh dấu hoàn thành");
            doneButton.setAllCaps(false);
            doneButton.setTextColor(getResources().getColor(R.color.white, null));
            doneButton.setBackgroundResource(R.drawable.bg_button_auth);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    dp(44)
            );
            buttonParams.setMargins(0, dp(12), 0, 0);
            body.addView(doneButton, buttonParams);
            doneButton.setOnClickListener(v -> markComplaintDone(item.getId()));
        }

        card.setOnClickListener(v -> showComplaintDetail(item));
        card.addView(body);
        adminComplaintList.addView(card);
    }

    private void markComplaintDone(Long complaintId) {
        complaintApiService.updateStatus(complaintId, new UpdateComplaintStatusRequest("DONE"))
                .enqueue(new Callback<ApiResponse<ComplaintResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<ComplaintResponse>> call,
                                           Response<ApiResponse<ComplaintResponse>> response) {
                        if (response.isSuccessful()) {
                            loadComplaints();
                            Toast.makeText(requireContext(), "Đã cập nhật khiếu nại", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
        String message = "Tiêu đề: " + safe(item.getTitle())
                + "\n\nNội dung:\n" + safe(item.getContent())
                + "\n\nGửi tới: " + getNotificationTargetText(item)
                + "\nNgày gửi: " + formatDate(item.getCreatedAt());

        new AlertDialog.Builder(requireContext())
                .setTitle("Chi tiết thông báo")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void showComplaintDetail(ComplaintResponse item) {
        String message = "Tiêu đề: " + safe(item.getTitle())
                + "\n\nNội dung:\n" + safe(item.getContent())
                + "\n\nCăn hộ: " + safe(item.getRoomNumber())
                + "\nNgười gửi: " + safe(item.getResidentName())
                + "\nLoại: " + translateCategory(item.getCategory())
                + "\nTrạng thái: " + translateStatus(item.getStatus())
                + "\nNgày gửi: " + formatDate(item.getCreatedAt());

        new AlertDialog.Builder(requireContext())
                .setTitle("Chi tiết khiếu nại")
                .setMessage(message)
                .setPositiveButton("Đóng", null)
                .show();
    }

    private int getStatusColor(String status) {
        if (status.contains("Mới") || status.contains("Đang")) {
            return getResources().getColor(R.color.primary, null);
        }
        if (status.contains("gửi") || status.contains("tiếp nhận")) {
            return getResources().getColor(R.color.indicator_current, null);
        }
        return getResources().getColor(R.color.text_secondary, null);
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

    private String translateTargetType(String targetType) {
        if ("SPECIFIC".equals(targetType)) return "Căn hộ được chọn";
        return "Tất cả chủ hộ";
    }

    private String getNotificationTargetText(NotificationResponse item) {
        if (item.getTargetSummary() != null && !item.getTargetSummary().trim().isEmpty()) {
            return item.getTargetSummary();
        }
        return translateTargetType(item.getTargetType());
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "Không có dữ liệu" : value;
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
