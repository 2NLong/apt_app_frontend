# 📱 APT Mobile App (Apartment Management System)

Đây là ứng dụng di động (Frontend) dành cho Cư dân và Ban quản lý thuộc dự án Quản lý Chung cư (APT). Ứng dụng được xây dựng hoàn toàn bằng **Java (Native)** và áp dụng kiến trúc **MVVM**, giúp phân tách luồng dữ liệu và giao diện một cách rõ ràng.

---

## 🚀 Giới thiệu chung

Ứng dụng đóng vai trò là Client, kết nối trực tiếp tới các RESTful API của hệ thống Backend (Spring Boot), cung cấp các tính năng chính như:
- Xác thực: Đăng nhập thường, Quên mật khẩu, Đổi mật khẩu.
- Cư dân (User): Xem thông tin dịch vụ, hợp đồng căn hộ, lịch sử hóa đơn.
- Quản trị (Admin/Staff/Accountant): Cung cấp giao diện quản lý trên thiết bị di động. Admin cấu hình dịch vụ; Staff quản lý hợp đồng cư dân; Accountant theo dõi hóa đơn và thanh toán.

---

## ⚙️ Công nghệ & Thư viện sử dụng

- **Ngôn ngữ:** Java 11 (Android)
- **Kiến trúc:** MVVM (Model-View-ViewModel)
- **Giao diện:** ViewBinding, Material Components, Navigation Component
- **Networking:** Retrofit2, OkHttp3 (Kèm Logging Interceptor)
- **Parsing JSON:** Gson
- **State Management:** ViewModel & LiveData
- **Bảo mật:** EncryptedSharedPreferences (Lưu trữ Token an toàn bằng mã hóa)

---

## 📂 Cấu trúc thư mục chuẩn

```text
app_frontend/apt_app_frontend/app/src/main/java/com/ptithcm/apt/
├── AptApplication.java   # Class Application chứa các cấu hình khởi tạo toàn cục của App
├── activities/           # Chứa các Activity chính đóng vai trò Container (Ví dụ: MainActivity)
├── fragments/            # Chứa các Fragment phục vụ Navigation (UI thực tế của từng màn hình)
├── adapters/             # RecyclerView Adapters dùng để hiển thị các danh sách dữ liệu
├── models/               # Data models (Entities/DTO) dùng để mapping dữ liệu trả về từ API
├── network/              # Thiết lập Retrofit, ApiService Interfaces và OkHttp Interceptors
├── repositoris/          # (Repositories) Tầng trung gian gọi API và cung cấp dữ liệu cho ViewModel
├── viewmodel/            # Chứa các ViewModel (MVVM) quản lý state (LiveData) và logic
├── enums/                # Định nghĩa các Enum (Loại thông báo, Trạng thái...)
└── utils/                # Helper / Utils (Các hàm tiện ích dùng chung)
```

> **Ghi chú:** Package `repositoris` hiện tại là nơi chứa các Repository pattern của project.

---

## 📡 Cấu hình kết nối API (Backend)

Ứng dụng kết nối tới Backend thông qua REST API. Để ứng dụng gọi API thành công, bạn cần đảm bảo Backend đang chạy và trỏ đúng IP.

👉 **Vị trí cấu hình IP API:**
Hãy tìm class `RetrofitClient` hoặc file cấu hình BASE_URL trong package `network/` và thay đổi:
- Nếu chạy **Android Emulator** kết nối về máy tính đang chạy server: dùng IP `http://10.0.2.2:8080/`
- Nếu chạy **Điện thoại thật**: dùng IP mạng LAN của máy tính đang chạy Backend (Ví dụ: `http://192.168.1.x:8080/`)

---

## ▶️ Hướng dẫn cài đặt & Chạy dự án

1. **Clone dự án** và mở thư mục `apt_app_frontend` bằng **Android Studio**.
2. Đợi Android Studio **Sync Gradle** thành công tải các thư viện.
3. Nhấn **Run 'app'** (Shift + F10) để chạy ứng dụng trên máy ảo (Emulator) hoặc điện thoại Android thật.

---

## 📌 Định hướng phát triển & Best Practices

- Tuân thủ chặt chẽ luồng dữ liệu **MVVM**: `View` (Activity/Fragment) -> `ViewModel` -> `Repository` -> `Network/Local API`. Không gọi trực tiếp API từ Fragment/Activity.
- Tách biệt logic kinh doanh khỏi xử lý UI (Giao diện).
- Khi thêm màn hình mới, sử dụng `Navigation Component` để điều hướng mượt mà giữa các Fragments thay vì tạo nhiều Activities.
