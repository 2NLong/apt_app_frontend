# 📱 Android Frontend Project (Java - MVVM)

## 🚀 Giới thiệu

Đây là project Android chỉ tập trung vào **Frontend (FE)**, sử dụng Java và kiến trúc **MVVM**.
Ứng dụng gọi API từ backend và hiển thị dữ liệu lên giao diện.

---

## 🧱 Cấu trúc project

```
com.yourapp
│
├── activities/        // Activity chính
├── fragments/         // UI tách nhỏ
├── adapters/          // RecyclerView adapter
├── models/            // Data model (Product, User…)
├── network/
│   ├── api/           // Interface Retrofit
│   └── retrofit/      // Config Retrofit
├── repository/        // Gọi API, xử lý dữ liệu
├── viewmodel/         // ViewModel (MVVM)
├── utils/             // Helper
│
└── res/
    ├── layout/        // Giao diện XML
    ├── drawable/      // Hình ảnh, background
    ├── mipmap/        // Icon app
    ├── values/        // colors, strings, themes
    ├── menu/          // Toolbar, Bottom Navigation
```

---

## ⚙️ Công nghệ sử dụng

* Java
* MVVM Architecture
* Retrofit (call API)
* Gson (parse JSON)
* RecyclerView
* Glide (load image)

---

## 📡 API

Ứng dụng kết nối tới backend thông qua REST API.
Bạn có thể thay đổi `BASE_URL` trong:

```
network/retrofit/RetrofitClient.java
```

---

## ▶️ Cách chạy project

1. Clone repo:

```
git clone https://github.com/your-username/your-repo.git
```

2. Mở bằng Android Studio

3. Sync Gradle

4. Run app trên emulator hoặc thiết bị thật

---

## 🔥 Tính năng (đang phát triển)

* [ ] Hiển thị danh sách sản phẩm
* [ ] Chi tiết sản phẩm
* [ ] Giỏ hàng
* [ ] Đăng nhập / đăng ký
* [ ] Gọi API theo category

---

## 📌 Định hướng

* Tách frontend/backend rõ ràng
* Code theo chuẩn MVVM
* Dễ mở rộng và maintain

---

## 👨‍💻 Author

* Name: Your Name
* GitHub: https://github.com/your-username

```
```
