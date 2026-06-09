# PRODUCT REQUIREMENT DOCUMENT (PRD)

# UMT Academic Assistant

### AI-Powered Academic Productivity Platform

Version: 1.0

Status: Approved

Platform: Desktop Application (Java Swing)

Target Release: UAS OOP 2026

---

# 1. Executive Summary

UMT Academic Assistant merupakan aplikasi desktop berbasis Artificial Intelligence yang dirancang untuk membantu mahasiswa Universitas Muhammadiyah Tangerang (UMT) dalam mengelola aktivitas akademik sehari-hari.

Aplikasi ini menggabungkan manajemen tugas, manajemen mata kuliah, manajemen catatan pembelajaran, serta fitur AI yang mampu membantu mahasiswa dalam menyusun prioritas belajar, mengevaluasi kondisi akademik, dan menghasilkan latihan soal secara otomatis.

Tujuan utama aplikasi adalah meningkatkan produktivitas akademik mahasiswa melalui pemanfaatan AI yang terintegrasi langsung dengan data akademik yang dimiliki pengguna.

---

# 2. Problem Statement

Mahasiswa sering mengalami berbagai kendala akademik seperti:

* Kesulitan mengelola banyak tugas dengan deadline yang berdekatan.
* Tidak mengetahui tugas mana yang harus diprioritaskan terlebih dahulu.
* Tidak memiliki gambaran kondisi akademik secara keseluruhan.
* Kesulitan membuat latihan soal untuk persiapan kuis dan ujian.
* Menyimpan data akademik di berbagai aplikasi yang terpisah.

Akibatnya mahasiswa sering mengalami keterlambatan pengerjaan tugas, penumpukan pekerjaan, serta proses belajar yang kurang efektif.

---

# 3. Product Vision

Menjadi asisten akademik berbasis AI yang membantu mahasiswa mengelola aktivitas belajar secara lebih terstruktur, produktif, dan efisien.

---

# 4. Product Goals

## Goal 1

Membantu mahasiswa mengelola data akademik dalam satu aplikasi.

---

## Goal 2

Membantu mahasiswa menentukan prioritas belajar berdasarkan kondisi akademik yang dimiliki.

---

## Goal 3

Memberikan insight akademik yang dapat membantu mahasiswa meningkatkan produktivitas belajar.

---

## Goal 4

Membantu mahasiswa melakukan latihan mandiri melalui soal yang dihasilkan AI.

---

# 5. Target Users

## Primary User

Mahasiswa Universitas Muhammadiyah Tangerang.

Karakteristik:

* Semester 1 hingga semester 8.
* Memiliki beberapa mata kuliah aktif.
* Memiliki banyak tugas dengan deadline yang berbeda.
* Membutuhkan bantuan dalam mengatur aktivitas belajar.

---

# 6. User Pain Points

## Pain Point 1

Mahasiswa lupa deadline tugas.

---

## Pain Point 2

Mahasiswa tidak mengetahui prioritas pengerjaan tugas.

---

## Pain Point 3

Mahasiswa tidak memiliki evaluasi akademik sederhana yang mudah dipahami.

---

## Pain Point 4

Mahasiswa kesulitan membuat soal latihan berdasarkan materi kuliah.

---

## In Scope

### Simple User Authentication

Sistem masuk desktop sederhana untuk meningkatkan profesionalisme sebelum mengakses aplikasi.

---

### Dashboard

Menampilkan ringkasan data akademik pengguna.

---

### Mata Kuliah

Manajemen data mata kuliah.

---

### Tugas

Manajemen data tugas akademik.

---

### Catatan

Manajemen catatan pembelajaran.

---

### AI Assistant

Fitur berbasis Gemini AI yang terintegrasi dengan data akademik.

---

## Out of Scope

Fitur berikut tidak termasuk pada versi UAS:

* Registrasi Mandiri / Register Online
* Multi User dengan kepemilikan data (ownership)
* Role Management
* Kalender Akademik
* Upload File
* Export PDF
* Notifikasi Real-Time
* Cloud Storage
* Mobile Application
* Integrasi SIAKAD
* Integrasi LMS


---

# 8. Core Features

## Feature 0

### Simple User Authentication

Deskripsi:

Sistem login desktop untuk mengamankan akses aplikasi.

Fungsi:

* Verifikasi kredensial username dan password.
* Inisialisasi otomatis akun default (admin/admin123) pada peluncuran pertama.
* Tampilan pesan error interaktif untuk kombinasi salah atau input kosong.

---

## Feature 1


### Course Management

Deskripsi:

Mahasiswa dapat mengelola data mata kuliah yang sedang diambil.

Fungsi:

* Tambah Mata Kuliah
* Edit Mata Kuliah
* Hapus Mata Kuliah
* Lihat Mata Kuliah

---

## Feature 2

### Task Management

Deskripsi:

Mahasiswa dapat mengelola seluruh tugas akademik.

Fungsi:

* Tambah Tugas
* Edit Tugas
* Hapus Tugas
* Lihat Tugas

Status:

* Belum Dikerjakan
* Sedang Dikerjakan
* Selesai

---

## Feature 3

### Note Management

Deskripsi:

Mahasiswa dapat menyimpan catatan pembelajaran.

Fungsi:

* Tambah Catatan
* Edit Catatan
* Hapus Catatan
* Lihat Catatan

---

# 9. AI Features

## AI Feature 1

### Smart Study Planner

Tujuan:

Membantu mahasiswa menentukan prioritas belajar berdasarkan data tugas yang tersedia.

Input:

* Daftar Tugas
* Deadline
* Status Tugas

Output:

* Prioritas Tugas
* Jadwal Belajar
* Rekomendasi Aktivitas Belajar

Expected Outcome:

Mahasiswa mengetahui tugas yang harus diprioritaskan terlebih dahulu.

---

## AI Feature 2

### Academic Health Analyzer

Tujuan:

Mengevaluasi kondisi akademik mahasiswa secara keseluruhan.

Input:

* Data Mata Kuliah
* Data Tugas
* Deadline
* Status Tugas

Output:

* Academic Health Score
* Insight Akademik
* Risiko Akademik
* Rekomendasi Perbaikan

Expected Outcome:

Mahasiswa memahami kondisi akademiknya saat ini.

---

## AI Feature 3

### Quiz Generator

Tujuan:

Menghasilkan latihan soal berdasarkan catatan yang dimiliki mahasiswa.

Input:

* Catatan Pembelajaran

Output:

* Soal Pilihan Ganda
* Soal Essay
* Referensi Jawaban

Expected Outcome:

Mahasiswa memiliki sarana belajar mandiri yang lebih efektif.

---

# 10. User Journey

## Skenario 1

Mahasiswa membuka aplikasi.

↓

Mengisi username dan password pada halaman Login.

↓

Melihat Dashboard setelah verifikasi sukses.


↓

Menambahkan Mata Kuliah.

↓

Menambahkan Tugas.

↓

Menambahkan Catatan.

↓

Data tersimpan.

---

## Skenario 2

Mahasiswa membuka menu AI.

↓

Memilih Smart Study Planner.

↓

AI menganalisis tugas.

↓

AI menghasilkan prioritas belajar.

---

## Skenario 3

Mahasiswa membuka menu AI.

↓

Memilih Academic Health Analyzer.

↓

AI melakukan evaluasi akademik.

↓

AI menghasilkan Academic Health Score.

---

## Skenario 4

Mahasiswa memilih Catatan.

↓

Memilih Generate Quiz.

↓

AI menghasilkan soal latihan.

↓

Mahasiswa menggunakan soal tersebut untuk belajar.

---

# 11. Success Metrics

## Functional Metrics

* Seluruh CRUD berjalan dengan baik.
* Data tersimpan ke database.
* AI berhasil memberikan respons.

---

## Product Metrics

* Mahasiswa dapat membuat jadwal belajar otomatis.
* Mahasiswa dapat memperoleh evaluasi akademik.
* Mahasiswa dapat menghasilkan soal latihan.

---

# 12. MVP Definition

Versi MVP harus memenuhi:

* Dashboard
* CRUD Mata Kuliah
* CRUD Tugas
* CRUD Catatan
* Smart Study Planner
* Academic Health Analyzer
* Quiz Generator

Jika seluruh fitur di atas berjalan dengan baik maka aplikasi dianggap siap untuk demonstrasi UAS.

---

# 13. Product Value Proposition

UMT Academic Assistant tidak hanya berfungsi sebagai aplikasi pencatatan akademik, tetapi juga sebagai asisten belajar berbasis AI yang mampu mengubah data akademik mahasiswa menjadi insight yang membantu pengambilan keputusan belajar secara lebih efektif.
