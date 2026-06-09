# PRODUCT REQUIREMENT DOCUMENT (PRD)

# UMT Academic Assistant

### AI-Powered Academic Productivity Platform

Version: 1.0 (Final Release)

Status: Approved / Release Candidate

Platform: Desktop Application (Java Swing, FlatLaf UI)

Target Release: UAS OOP 2026

---

# 1. Executive Summary

UMT Academic Assistant merupakan aplikasi desktop berbasis Artificial Intelligence yang dirancang untuk membantu mahasiswa Universitas Muhammadiyah Tangerang (UMT) dalam mengelola aktivitas akademik sehari-hari secara aman dan mandiri.

Aplikasi ini mengintegrasikan fitur login/register multi-user, manajemen mata kuliah, tugas, catatan perkuliahan, serta fitur kecerdasan buatan (AI) yang dihubungkan melalui Replicate API (Google Gemini 2.5 Flash) untuk membantu menyusun rencana belajar mingguan, menganalisis kesehatan produktivitas akademik, dan menghasilkan kuis latihan interaktif secara instan.

Tujuan utama dari aplikasi ini adalah meningkatkan efisiensi dan hasil belajar mahasiswa UMT melalui asisten pribadi pintar yang menjaga privasi data antar-pengguna secara ketat.

---

# 2. Problem Statement

Mahasiswa UMT sering menghadapi berbagai kendala produktivitas seperti:
* **Overwhelming Workload**: Mengelola banyak tugas dari berbagai mata kuliah dengan deadline yang berdekatan tanpa adanya prioritas yang jelas.
* **Lack of Analytics**: Tidak adanya visualisasi atau diagnosis objektif mengenai tingkat produktivitas dan status kesehatan akademik mereka sendiri.
* **Ineffective Self-Study**: Kesulitan dalam menguji pemahaman materi kuliah secara mandiri tanpa adanya sarana kuis interaktif yang sesuai dengan catatan perkuliahan mereka.
* **Privacy & Security**: Kebutuhan untuk menyimpan data perkuliahan, tugas, dan catatan secara aman pada satu platform desktop tanpa tercampur dengan pengguna lain.

---

# 3. Product Vision

Menjadi asisten akademis desktop berbasis AI terkemuka untuk mahasiswa UMT yang menggabungkan manajemen mandiri data kuliah dengan analisis cerdas (AI) untuk menciptakan lingkungan belajar yang terstruktur, aman, dan berkinerja tinggi.

---

# 4. Product Goals

## Goal 1: Secure Data Isolation
Menyediakan modul otentikasi login, register, dan manajemen sesi yang aman sehingga setiap mahasiswa memiliki ruang penyimpanan data akademik yang terisolasi sepenuhnya (data ownership).

## Goal 2: Intelligent Study Planning
Menyusun rekomendasi jadwal belajar mingguan yang logis berdasarkan beban perkuliahan dan tanggal batas waktu pengumpulan tugas perkuliahan secara otomatis lewat AI.

## Goal 3: Productivity Health Diagnostics
Memberikan metrik evaluasi kesehatan produktivitas akademik berupa skor (0-100), analisis kekuatan, area perbaikan, dan rekomendasi mitigasi risiko dari AI secara objektif.

## Goal 4: Active Self-Assessment
Menyediakan kuis interaktif 5 soal pilihan ganda yang dihasilkan otomatis oleh AI dari konten catatan pembelajaran perkuliahan, lengkap dengan kalkulasi nilai akhir dan review jawaban.

---

# 5. Target Users

## Primary User: Mahasiswa Universitas Muhammadiyah Tangerang (UMT)
Karakteristik:
* Mengambil beberapa mata kuliah aktif tiap semester.
* Memiliki banyak tugas dengan tenggat waktu bervariasi.
* Menyimpan rangkuman catatan kuliah dan memerlukan sarana latihan soal.
* Menginginkan asisten pintar yang dapat membantu menyusun prioritas belajar secara cepat.

---

# 6. User Stories

* **Sebagai** mahasiswa UMT, **saya ingin** mendaftar akun baru dan login secara aman, **sehingga** data mata kuliah, tugas, dan catatan saya terjaga privasinya dari mahasiswa lain.
* **Sebagai** mahasiswa UMT, **saya ingin** melihat statistik jumlah kuliah, tugas selesai, tugas tertunda, serta catatan saya di dashboard utama, **sehingga** saya mengetahui progres akademik secara sekilas.
* **Sebagai** mahasiswa UMT, **saya ingin** AI menyusun rencana belajar mingguan dari daftar tugas saya, **sehingga** saya tahu tugas mana yang harus diselesaikan terlebih dahulu.
* **Sebagai** mahasiswa UMT, **saya ingin** AI mengevaluasi beban belajar saya dan menampilkan skor produktivitas beserta area perbaikan, **sehingga** saya bisa memperbaiki kebiasaan belajar saya.
* **Sebagai** mahasiswa UMT, **saya ingin** membuat kuis latihan interaktif dari catatan perkuliahan saya, **sehingga** saya bisa menguji pemahaman saya sebelum ujian.

---

# 7. Product Scope

## In Scope

### 1. Advanced Authentication
* Halaman login dan register desktop menggunakan FlatLaf.
* Enkripsi sesi dan isolasi data per pengguna (`user_id`).
* Inisialisasi akun administrator default (`admin` / `admin123`) saat startup pertama.

### 2. Personalized Dashboard
* Statistik akademik real-time (Total Mata Kuliah, Total Tugas, Tugas Selesai, Tugas Tertunda, Total Catatan).
* Sambutan nama pengguna dinamis ("Selamat Datang, [Nama User]") dan tampilan kalender/tanggal aktif.
* Daftar Tugas Mendatang (sorted by deadline) dengan indikator kedekatan deadline berwarna (Merah, Oranye, Biru).

### 3. Academic Management (CRUD)
* **Mata Kuliah**: Kode kuliah, nama kuliah, jumlah SKS, nama dosen.
* **Tugas**: Mata kuliah terelasi, judul tugas, pemilih tanggal kalender, status (Belum Dikerjakan, Sedang Dikerjakan, Selesai).
* **Catatan**: Relasi mata kuliah, judul catatan, teks konten catatan lengkap.

### 4. AI Assistant
* **Smart Study Planner**: Rencana prioritas belajar mingguan dalam bentuk kartu laporan, lengkap dengan waktu pembuatan dan tombol "Salin Hasil".
* **Academic Health Analyzer**: Laporan diagnosis kesehatan akademik dengan indikator skor berwarna (Merah, Oranye, Hijau) beserta rekomendasi AI.
* **Quiz Generator 2.0**: Kuis interaktif 5 pilihan ganda (A-D) dengan navigasi soal, kalkulasi skor akhir, dan panel review jawaban detail.

## Out of Scope
* Multi-user online database cloud storage (aplikasi menggunakan database lokal MySQL).
* Integrasi langsung dengan SIAKAD UMT atau LMS external (input data bersifat manual).
* Notifikasi email atau WhatsApp.
* Export laporan dalam format PDF atau Excel.

---

# 8. Success Metrics

* **Keberhasilan Otentikasi**: 100% data tugas, catatan, dan kuliah terpisah antar user ID dan hanya tampil jika user tersebut login.
* **Akurasi AI Parser**: AI menghasilkan format kuis dan analisis yang dapat diproses dan dibaca secara akurat oleh sistem untuk kuis interaktif dan indikator skor kesehatan.
* **Kecepatan Respon**: SwingWorker memastikan antarmuka Swing tidak freeze selama pengolahan data AI (Replicate API) di latar belakang.
