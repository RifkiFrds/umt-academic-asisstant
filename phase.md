# UMT Academic Assistant

## Final Development Phase Blueprint v1.0

### Project Information

Project Name:
UMT Academic Assistant

Platform:
Desktop Application

Architecture:
Model View Controller (MVC)

Programming Language:
Java 17

GUI Framework:
Java Swing

UI Theme:
FlatLaf

Database:
MySQL

Database Connectivity:
JDBC

HTTP Client:
Java HttpClient

AI Integration:
Gemini API

JSON Parser:
Gson

---

# Fixed Package Structure

src/

model/
├── Course.java
├── Task.java
├── Note.java
├── TaskStatus.java

view/
├── DashboardView.java
├── CourseView.java
├── TaskView.java
├── NoteView.java
├── AIView.java

controller/
├── CourseController.java
├── TaskController.java
├── NoteController.java
├── AIController.java

dao/
├── CourseDAO.java
├── TaskDAO.java
├── NoteDAO.java

database/
├── DBConnection.java

service/
├── GeminiService.java

utils/
├── ConfigReader.java
├── ThemeManager.java

main/
├── Main.java

---

# Development Rules

1. Wajib mengikuti package structure di atas.
2. Tidak boleh membuat package baru tanpa kebutuhan yang jelas.
3. Tidak boleh mengubah arsitektur MVC.
4. Tidak boleh menambahkan framework selain yang telah ditentukan.
5. Seluruh database access harus melalui DAO.
6. View tidak boleh mengakses database secara langsung.
7. View hanya berkomunikasi dengan Controller.
8. Controller menjadi penghubung View dan DAO.
9. Gemini API hanya boleh diakses melalui GeminiService.
10. Semua kode harus kompatibel dengan Java 17.

---

# PHASE 0 — Project Foundation

Objective:
Menyiapkan struktur proyek dan dependensi dasar.

Deliverables:

* Struktur package MVC
* FlatLaf setup
* JDBC setup
* Gson setup
* Config file setup
* Main.java setup

Checklist:

[ ] Package structure dibuat
[ ] Main.java dibuat
[ ] FlatLaf berhasil dijalankan
[ ] JDBC dependency tersedia
[ ] Gson dependency tersedia

Definition of Done:

Aplikasi dapat dijalankan dan menampilkan window kosong menggunakan FlatLaf.

---

# PHASE 1 — Database Foundation

Objective:
Membangun database sesuai SSD.

Deliverables:

* schema.sql

Tables:

1. courses
2. tasks
3. notes

Checklist:

[ ] Create courses table
[ ] Create tasks table
[ ] Create notes table
[ ] Foreign key berhasil
[ ] Dummy data tersedia

Definition of Done:

Seluruh tabel berhasil dibuat di MySQL tanpa error.

---

# PHASE 2 — Domain Model Layer

Objective:
Membangun representasi object.

Deliverables:

model/

Files:

* Course.java
* Task.java
* Note.java
* TaskStatus.java

Checklist:

[ ] Constructor
[ ] Getter
[ ] Setter
[ ] toString()

Definition of Done:

Semua model dapat digunakan oleh DAO.

---

# PHASE 3 — Infrastructure Layer

Objective:
Membangun utilitas dasar aplikasi.

Deliverables:

database/
utils/

Files:

* DBConnection.java
* ConfigReader.java
* ThemeManager.java

Checklist:

[ ] DBConnection berhasil connect
[ ] ConfigReader membaca file config
[ ] ThemeManager menjalankan FlatLaf

Definition of Done:

Database dapat diakses dari aplikasi.

---

# PHASE 4 — DAO Layer

Objective:
Membangun seluruh akses data.

Deliverables:

dao/

Files:

* CourseDAO.java
* TaskDAO.java
* NoteDAO.java

Checklist:

CourseDAO
[ ] Insert
[ ] Update
[ ] Delete
[ ] Get All

TaskDAO
[ ] Insert
[ ] Update
[ ] Delete
[ ] Get All

NoteDAO
[ ] Insert
[ ] Update
[ ] Delete
[ ] Get All

Definition of Done:

CRUD seluruh entity berhasil melalui database.

---

# PHASE 5 — Controller Layer

Objective:
Membangun business logic layer.

Deliverables:

controller/

Files:

* CourseController.java
* TaskController.java
* NoteController.java

Checklist:

[ ] Input validation
[ ] DAO integration
[ ] Error handling

Definition of Done:

View dapat menggunakan seluruh fitur CRUD melalui controller.

---

# PHASE 6 — Course Management Module

Objective:
Menyelesaikan fitur Course Management.

Deliverables:

CourseView.java

Features:

[ ] Add Course
[ ] Edit Course
[ ] Delete Course
[ ] View Course

Definition of Done:

CRUD Mata Kuliah berjalan penuh.

---

# PHASE 7 — Task Management Module

Objective:
Menyelesaikan fitur Task Management.

Deliverables:

TaskView.java

Features:

[ ] Add Task
[ ] Edit Task
[ ] Delete Task
[ ] View Task

Definition of Done:

CRUD Tugas berjalan penuh.

---

# PHASE 8 — Note Management Module

Objective:
Menyelesaikan fitur Note Management.

Deliverables:

NoteView.java

Features:

[ ] Add Note
[ ] Edit Note
[ ] Delete Note
[ ] View Note

Definition of Done:

CRUD Catatan berjalan penuh.

---

# PHASE 9 — Dashboard Module

Objective:
Menampilkan statistik akademik.

Deliverables:

DashboardView.java

Components:

[ ] Total Courses
[ ] Total Tasks
[ ] Completed Tasks
[ ] Pending Tasks

Definition of Done:

Dashboard menampilkan statistik secara realtime.

---

# PHASE 10 — AI Foundation

Objective:
Integrasi Gemini API.

Deliverables:

service/

Files:

* GeminiService.java

Checklist:

[ ] API Key Configuration
[ ] HTTP Request
[ ] HTTP Response
[ ] Gson Parsing
[ ] Error Handling

Definition of Done:

Gemini API dapat memberikan respons.

---

# PHASE 11 — Smart Study Planner

Objective:
Membuat prioritas belajar otomatis.

Dependencies:

* TaskDAO
* TaskController
* GeminiService

Features:

[ ] Ambil data tugas
[ ] Generate prompt
[ ] Kirim ke Gemini
[ ] Tampilkan hasil

Definition of Done:

Study Plan berhasil dihasilkan AI.

---

# PHASE 12 — Academic Health Analyzer

Objective:
Mengevaluasi kondisi akademik.

Dependencies:

* CourseDAO
* TaskDAO
* GeminiService

Features:

[ ] Ambil data akademik
[ ] Generate prompt
[ ] Analisis AI
[ ] Tampilkan hasil

Definition of Done:

Academic Health Score berhasil ditampilkan.

---

# PHASE 13 — Quiz Generator

Objective:
Menghasilkan latihan soal.

Dependencies:

* NoteDAO
* GeminiService

Features:

[ ] Ambil catatan
[ ] Generate prompt
[ ] Generate quiz
[ ] Tampilkan hasil

Definition of Done:

Quiz berhasil dibuat berdasarkan catatan.

---

# PHASE 14 — AI Center Integration

Objective:
Menggabungkan seluruh fitur AI.

Deliverables:

AIView.java
AIController.java

Tabs:

1. Study Planner
2. Academic Health
3. Quiz Generator

Definition of Done:

Semua fitur AI dapat diakses dari AI Center.

---

# PHASE 15 — Final Testing & UAS Preparation

Objective:
Menyiapkan aplikasi untuk demonstrasi.

Checklist:

[ ] Test CRUD Course
[ ] Test CRUD Task
[ ] Test CRUD Note
[ ] Test Dashboard
[ ] Test Gemini Integration
[ ] Test Study Planner
[ ] Test Academic Health
[ ] Test Quiz Generator
[ ] Test Error Handling
[ ] Siapkan Dummy Data Demo

Definition of Done:

Aplikasi siap dipresentasikan pada UAS OOP.
