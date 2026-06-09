# SYSTEM DESIGN DOCUMENT (SSD)

# UMT Academic Assistant

### AI-Powered Academic Productivity Platform

Version: 1.0 (Final Release)

Status: Approved / Final

---

# 1. System Architecture

## Architecture Style
Model View Controller (MVC) pattern.

Tujuan:
* Memisahkan antarmuka (Swing GUI), logika kendali (Controller), dan data (Model + DAO).
* Mengamankan data dengan membatasi akses database berdasarkan context session pengguna aktif (`SessionManager`).
* Menjaga responsivitas UI dengan thread latar belakang (`SwingWorker`) untuk pengolahan data AI eksternal.

## High Level Architecture

```mermaid
flowchart TD
    User([Mahasiswa])
    
    subgraph View Layer (Swing & FlatLaf)
        LoginView
        RegisterView
        DashboardView
        CourseView
        TaskView
        NoteView
        AIView
    end
    
    subgraph Session Management
        SessionManager
    end
    
    subgraph Controller Layer
        LoginController
        RegisterController
        CourseController
        TaskController
        NoteController
        AIController
    end
    
    subgraph Service Layer
        AIService
    end
    
    subgraph Model & DAO Layer
        Model[User / Course / Task / Note]
        DAO[UserDAO / CourseDAO / TaskDAO / NoteDAO]
    end
    
    subgraph External Systems
        MySQL[(MySQL Local DB)]
        Replicate[Replicate API]
    end
    
    User --> View Layer
    View Layer --> Controller Layer
    Controller Layer --> SessionManager
    Controller Layer --> ServiceLayer
    Controller Layer --> DAO
    ServiceLayer --> Replicate
    DAO --> MySQL
    DAO -.-> Model
```

---

# 2. Package Structure

```text
src/
├── model/
│   ├── User.java
│   ├── Course.java
│   ├── Task.java
│   ├── Note.java
│   └── TaskStatus.java
├── view/
│   ├── LoginView.java
│   ├── RegisterView.java
│   ├── DashboardView.java
│   ├── CourseView.java
│   ├── TaskView.java
│   ├── NoteView.java
│   ├── AIView.java
│   ├── StudyPlannerPanel.java
│   ├── AcademicHealthPanel.java
│   └── QuizGeneratorPanel.java
├── controller/
│   ├── LoginController.java
│   ├── RegisterController.java
│   ├── CourseController.java
│   ├── TaskController.java
│   ├── NoteController.java
│   └── AIController.java
├── dao/
│   ├── UserDAO.java
│   ├── CourseDAO.java
│   ├── TaskDAO.java
│   └── NoteDAO.java
├── database/
│   └── DBConnection.java
├── service/
│   └── AIService.java
├── utils/
│   ├── ConfigReader.java
│   ├── ThemeManager.java
│   └── SessionManager.java
└── main/
    └── Main.java
```

---

# 3. Database Design

## Entity Relationship Diagram

```mermaid
erDiagram
    USERS ||--o{ COURSES : owns
    USERS ||--o{ TASKS : owns
    USERS ||--o{ NOTES : owns
    COURSES ||--o{ TASKS : contains
    COURSES ||--o{ NOTES : contains

    USERS {
        int id PK
        varchar full_name
        varchar username UK
        varchar password
        timestamp created_at
    }

    COURSES {
        int id PK
        int user_id FK
        varchar course_code
        varchar course_name
        int sks
        varchar lecturer
    }

    TASKS {
        int id PK
        int user_id FK
        int course_id FK
        varchar title
        date deadline
        varchar status
    }

    NOTES {
        int id PK
        int user_id FK
        int course_id FK
        varchar title
        text content
    }
```

---

# 4. Class Diagram

```mermaid
classDiagram
    class User {
        -int id
        -String fullName
        -String username
        -String password
        -Timestamp createdAt
    }
    class Course {
        -int id
        -int userId
        -String courseCode
        -String courseName
        -int sks
        -String lecturer
    }
    class Task {
        -int id
        -int userId
        -int courseId
        -String title
        -LocalDate deadline
        -TaskStatus status
    }
    class Note {
        -int id
        -int userId
        -int courseId
        -String title
        -String content
    }

    class SessionManager {
        <<Utility>>
        -User currentUser
        +getCurrentUser() User
        +setCurrentUser(User)
        +clearSession()
        +isLoggedIn() boolean
    }

    class UserDAO {
        +login(String, String) User
        +register(User) boolean
        +usernameExists(String) boolean
        +getById(int) User
        +createDefaultAdminIfNotExists()
    }
    class CourseDAO {
        +insertCourse(Course)
        +updateCourse(Course)
        +deleteCourse(int)
        +getAllCourses() List~Course~
        +getCourseById(int) Course
    }
    class TaskDAO {
        +insertTask(Task)
        +updateTask(Task)
        +deleteTask(int)
        +getAllTasks() List~Task~
        +getTaskById(int) Task
    }
    class NoteDAO {
        +insertNote(Note)
        +updateNote(Note)
        +deleteNote(int)
        +getAllNotes() List~Note~
        +getNoteById(int) Note
    }

    class LoginController {
        -UserDAO userDAO
        +authenticate(String, String) User
        +initDefaultAdmin()
    }
    class RegisterController {
        -UserDAO userDAO
        +register(String, String, String, String) boolean
    }
    class CourseController {
        -CourseDAO courseDAO
        +getAllCourses() List~Course~
        +addCourse(String, String, int, String) boolean
        +updateCourse(int, String, String, int, String) boolean
        +deleteCourse(int) boolean
    }
    class TaskController {
        -TaskDAO taskDAO
        +getAllTasks() List~Task~
        +addTask(int, String, LocalDate, TaskStatus) boolean
        +updateTask(int, int, String, LocalDate, TaskStatus) boolean
        +deleteTask(int) boolean
    }
    class NoteController {
        -NoteDAO noteDAO
        +getAllNotes() List~Note~
        +addNote(int, String, String) boolean
        +updateNote(int, int, String, String) boolean
        +deleteNote(int) boolean
    }
    class AIController {
        -AIService aiService
        +generateStudyPlan(List~Course~, List~Task~) String
        +generateAcademicHealthAnalysis(int, int, long, long, int) String
        +generateQuiz(String, String) String
    }
    class AIService {
        +generateContent(String) String
    }

    LoginView ..> LoginController
    RegisterView ..> RegisterController
    DashboardView ..> CourseController
    DashboardView ..> TaskController
    DashboardView ..> NoteController
    CourseView ..> CourseController
    TaskView ..> TaskController
    TaskView ..> CourseController
    NoteView ..> NoteController
    NoteView ..> CourseController
    
    LoginController ..> UserDAO
    RegisterController ..> UserDAO
    CourseController ..> CourseDAO
    TaskController ..> TaskDAO
    NoteController ..> NoteDAO
    AIController ..> AIService
    
    UserDAO ..> User
    CourseDAO ..> Course
    TaskDAO ..> Task
    NoteDAO ..> Note
    
    CourseDAO ..> SessionManager
    TaskDAO ..> SessionManager
    NoteDAO ..> SessionManager
```

---

# 5. Sequence Diagrams

## 5.1 Login Flow

```mermaid
sequenceDiagram
    actor User as Mahasiswa
    participant LV as LoginView
    participant LC as LoginController
    participant UD as UserDAO
    participant SM as SessionManager
    participant MV as MainAppFrame

    User->>LV: Masukkan username & password
    User->>LV: Klik tombol Masuk
    LV->>LC: authenticate(username, password)
    LC->>UD: login(username, password)
    UD-->>LC: User Object (atau null)
    
    alt Kredensial Valid
        LC-->>LV: User Object
        LV->>SM: setCurrentUser(User)
        LV->>LV: dispose()
        LV->>MV: launchMainDashboard()
        MV-->>User: Tampilkan Dashboard
    else Kredensial Tidak Valid
        LC-->>LV: throw SQLException / IllegalArgumentException
        LV->>LV: Tampilkan pesan kesalahan
        LV-->>User: Refresh halaman Login
    end
```

## 5.2 Generate Study Plan (AI)

```mermaid
sequenceDiagram
    actor User as Mahasiswa
    participant SP as StudyPlannerPanel
    participant AC as AIController
    participant AS as AIService
    participant RE as Replicate API

    User->>SP: Klik "Generate Rencana Belajar"
    SP->>SP: Tampilkan Card Loading
    SP->>AC: generateStudyPlan(courses, tasks)
    AC->>AS: generateContent(prompt)
    AS->>RE: HTTP POST Request (Gemini 2.5 Flash)
    RE-->>AS: HTTP JSON Response (Plain Text)
    AS-->>AC: Respons Rencana Belajar
    AC-->>SP: Hasil Rencana Belajar (Bahasa Indonesia)
    SP->>SP: Tampilkan StudyPlanCard (Plan + Timestamp)
    SP-->>User: Tampilkan Rencana Belajar Mingguan
```

## 5.3 Generate & Mainkan Kuis (Interactive Mode)

```mermaid
sequenceDiagram
    actor User as Mahasiswa
    participant QP as QuizGeneratorPanel
    participant AC as AIController
    participant AS as AIService
    participant RE as Replicate API

    User->>QP: Pilih catatan perkuliahan
    User->>QP: Klik "Mulai Kuis"
    QP->>QP: Tampilkan Loading Card
    QP->>AC: generateQuiz(title, content)
    AC->>AS: generateContent(prompt)
    AS->>RE: HTTP POST Request (Kuis 5 PG + Kunci)
    RE-->>AS: HTTP Response
    AS-->>AC: Raw Text Quiz
    AC-->>QP: Raw Text Quiz
    QP->>QP: parseQuizQuestions(text) ke List~QuizQuestion~
    QP->>QP: Tampilkan Pertanyaan 1 dari 5
    loop Jawab Kuis
        User->>QP: Pilih opsi A/B/C/D
        User->>QP: Klik "Berikutnya"
    end
    User->>QP: Klik "Selesai Kuis"
    QP->>QP: Hitung skor & kumpulkan review jawaban
    QP->>QP: Tampilkan Skor Card & Review List
    QP-->>User: Tampilkan Nilai Akhir Kuis
```

## 5.4 CRUD Course (Mata Kuliah)

```mermaid
sequenceDiagram
    actor User as Mahasiswa
    participant CV as CourseView
    participant CC as CourseController
    participant CD as CourseDAO
    participant SM as SessionManager
    participant DB as MySQL Database

    User->>CV: Masukkan data mata kuliah
    User->>CV: Klik tombol Tambah
    CV->>CC: addCourse(code, name, sks, lecturer)
    CC->>CD: insertCourse(Course)
    CD->>SM: getCurrentUser().getId()
    SM-->>CD: activeUserId
    CD->>DB: INSERT INTO courses (user_id, ...) VALUES (activeUserId, ...)
    DB-->>CD: Success (Generated ID)
    CD-->>CC: Success
    CC-->>CV: true
    CV->>CC: getAllCourses()
    CC->>CD: getAllCourses()
    CD->>DB: SELECT ... WHERE user_id = activeUserId
    DB-->>CD: ResultSet
    CD-->>CC: List~Course~
    CC-->>CV: List~Course~
    CV->>CV: updateTable()
    CV-->>User: Tampilkan data baru di tabel
```

---

# 6. Navigation Flow

```mermaid
flowchart TD
    Login[LoginView] -->|Sukses| Dashboard[DashboardView]
    Login -->|Belum punya akun| Register[RegisterView]
    Register -->|Daftar Sukses| Login
    
    subgraph Main App Frame
        Dashboard -->|Navigasi Sidebar| Courses[CourseView]
        Dashboard -->|Navigasi Sidebar| Tasks[TaskView]
        Dashboard -->|Navigasi Sidebar| Notes[NoteView]
        Dashboard -->|Navigasi Sidebar| AI[AIView Playground]
        
        AI -->|Buka Card| StudyPlan[StudyPlannerPanel]
        AI -->|Buka Card| Health[AcademicHealthPanel]
        AI -->|Buka Card| Quiz[QuizGeneratorPanel]
    end

    Courses -->|Logout| Login
    Tasks -->|Logout| Login
    Notes -->|Logout| Login
    Dashboard -->|Logout| Login
    AI -->|Logout| Login
```
