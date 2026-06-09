# UMT Academic Assistant 🎓
> **AI-Powered Academic Productivity Desktop Platform**
> Developed for UAS Object-Oriented Programming (OOP) 2026.

UMT Academic Assistant adalah aplikasi desktop produktivitas akademik berbasis **Java Swing** dan **FlatLaf Look & Feel** yang dirancang khusus untuk mahasiswa Universitas Muhammadiyah Tangerang (UMT). Aplikasi ini terintegrasi dengan **MySQL** untuk penyimpanan lokal dan menggunakan **Google Gemini 2.5 Flash / 2.0 Flash Lite** via **Replicate API** untuk menyediakan asisten pintar berbasis AI.

---

## 🚀 Fitur Utama

### 🔒 1. Sistem Otentikasi & Kepemilikan Data (Multi-User)
* **Register & Login**: Mahasiswa dapat mendaftarkan akun baru secara lokal.
* **Isolasi Data**: Data mata kuliah, tugas, dan catatan perkuliahan terikat langsung ke `user_id` masing-masing pengguna untuk menjaga keamanan data.
* **Logout / Keluar**: Sesi aktif dapat diakhiri dengan aman melalui tombol Logout untuk kembali ke halaman Login.

### 📊 2. Dashboard Akademik Utama
* **Personalized Greeting**: Menyapa pengguna secara dinamis berdasarkan nama lengkap mereka.
* **Statistik Real-Time**: Menampilkan total mata kuliah aktif, tugas yang selesai, tugas tertunda, dan catatan kuliah yang disimpan.
* **Tugas Mendatang (Urgent Tasks)**: Panel khusus yang menyoroti tugas dengan tenggat waktu terdekat menggunakan kode warna visual:
  * 🔴 **Merah**: < 2 hari sebelum tenggat waktu.
  * 🟠 **Oranye**: < 5 hari sebelum tenggat waktu.
  * 🔵 **Biru**: Tingkat urgensi normal (>= 5 hari).

### 📝 3. Manajemen Akademik (CRUD)
* **Mata Kuliah**: Mengelola kode kuliah, nama kuliah, jumlah SKS, dan dosen pengampu.
* **Tugas**: Menghubungkan tugas dengan mata kuliah tertentu, dilengkapi dengan visual Calendar Datepicker dan status progres.
* **Catatan Kuliah**: Menulis dan mengorganisasikan catatan ringkasan materi kuliah secara detail.

### 🤖 4. Fitur Pintar AI (Bahasa Indonesia & Clean Text)
* **Smart Study Planner**: AI menganalisis seluruh beban tugas aktif untuk menyusun jadwal prioritas belajar mingguan terstruktur. Hasil rencana dapat langsung disalin ke clipboard.
* **Academic Health Analyzer**: Mendiagnosis performa akademik secara keseluruhan, menghitung Skor Produktivitas (0-100) dengan badge visual indikator kesehatan (Merah/Oranye/Hijau), serta memberikan rekomendasi tindakan yang personal.
* **Quiz Generator 2.0 (Interactive Mode)**: Mengonversi materi catatan kuliah menjadi kuis interaktif pilihan ganda (5 soal). Dilengkapi panel navigasi soal, feedback jawaban yang benar, serta skor akhir beserta ulasannya.

---

## 🛠️ Tech Stack & Prasyarat

* **Bahasa**: Java 17
* **GUI Framework**: Java Swing (FlatLaf Light Theme)
* **Database**: MySQL Server 8.x
* **Build System**: Maven 3.x
* **AI API**: Replicate API (Google Gemini 2.5 Flash)
* **Library Utama**:
  * `gson` untuk parsing response JSON dari AI.
  * `flatlaf` untuk UI modern berestetika premium.
  * `mysql-connector-j` untuk konektivitas database.

---

## ⚙️ Cara Instalasi & Konfigurasi

### 1. Inisialisasi Database
1. Pastikan server MySQL Anda telah aktif.
2. Buat database baru bernama `umt_academic_assistant`.
3. Jalankan skrip [schema.sql](file:///C:/.vscode/umt-academic-asisstant/schema.sql) untuk menginisialisasi tabel utama. 
4. *(Opsional)* Jika memperbarui dari versi lama, jalankan skrip migrasi di [alter_tables.sql](file:///C:/.vscode/umt-academic-asisstant/alter_tables.sql) atau biarkan aplikasi mendeteksi dan memperbarui kolom secara dinamis pada saat startup pertama kali.

### 2. File Konfigurasi
Buka file [application.properties](file:///C:/.vscode/umt-academic-asisstant/src/main/resources/application.properties) dan sesuaikan konfigurasi database serta token API Anda:

```properties
# Database Configuration
db.host=localhost
db.port=3306
db.name=umt_academic_assistant
db.username=root
db.password=your_mysql_password_here

# Gemini AI Configuration
gemini.api.key=your_gemini_key_here
gemini.api.url=https://generativelanguage.googleapis.com/v1beta/models/
gemini.model=gemini-2.0-flash-lite

# Replicate API Key (Sangat disarankan untuk kelancaran integrasi)
REPLICATE_API_TOKEN=r8_your_replicate_token_here

# Application Settings
app.theme=light
app.window.width=1280
app.window.height=720
```

---

## 🏎️ Menjalankan Aplikasi

### Kompilasi Proyek
Kompilasi source code menggunakan Maven:
```bash
mvn clean compile
```

### Jalankan Aplikasi
Jalankan kelas utama melalui perintah berikut:
```bash
mvn exec:java "-Dexec.mainClass=main.Main"
```

---

## 🔐 Akun Default untuk Pengujian
Aplikasi secara otomatis mendeteksi jika tabel pengguna kosong dan akan menginisialisasi satu akun administrator default untuk kenyamanan pengujian awal:
* **Username**: `admin`
* **Password**: `admin123`

---
