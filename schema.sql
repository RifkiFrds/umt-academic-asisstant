-- Database creation
CREATE DATABASE IF NOT EXISTS umt_academic_assistant;
USE umt_academic_assistant;

-- Drop tables if they exist (in reverse dependency order)
DROP TABLE IF EXISTS `notes`;
DROP TABLE IF EXISTS `tasks`;
DROP TABLE IF EXISTS `courses`;
DROP TABLE IF EXISTS `users`;

-- Table structure for table `users`
CREATE TABLE `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table structure for table `courses`
CREATE TABLE `courses` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_code` VARCHAR(20) NOT NULL,
    `course_name` VARCHAR(100) NOT NULL,
    `sks` INT NOT NULL,
    `lecturer` VARCHAR(100) NOT NULL,
    CONSTRAINT `fk_courses_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table structure for table `tasks`
CREATE TABLE `tasks` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `deadline` DATE NOT NULL,
    `status` VARCHAR(30) NOT NULL DEFAULT 'Belum Dikerjakan',
    CONSTRAINT `fk_tasks_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tasks_courses` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table structure for table `notes`
CREATE TABLE `notes` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `title` VARCHAR(150) NOT NULL,
    `content` TEXT NOT NULL,
    CONSTRAINT `fk_notes_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_notes_courses` FOREIGN KEY (`course_id`) REFERENCES `courses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Dummy Data for Default Admin User (id = 1)
INSERT INTO `users` (`id`, `full_name`, `username`, `password`) VALUES
(1, 'Administrator', 'admin', 'admin123');

-- Dummy Data for testing (associated with user_id = 1)
INSERT INTO `courses` (`user_id`, `course_code`, `course_name`, `sks`, `lecturer`) VALUES
(1, 'INF201', 'Pemrograman Berorientasi Objek', 3, 'Dr. Ir. Budi Santoso, M.T.'),
(1, 'INF202', 'Basis Data', 3, 'Siti Aminah, M.Kom.'),
(1, 'INF203', 'Rekayasa Perangkat Lunak', 3, 'Hendra Wijaya, S.T., M.T.');

INSERT INTO `tasks` (`user_id`, `course_id`, `title`, `deadline`, `status`) VALUES
(1, 1, 'Membuat Diagram Kelas MVC', '2026-06-15', 'Sedang Dikerjakan'),
(1, 1, 'Implementasi DAO Pattern', '2026-06-20', 'Belum Dikerjakan'),
(1, 2, 'Normalisasi Database Academic', '2026-06-12', 'Selesai'),
(1, 3, 'Penyusunan Dokumen SRS', '2026-06-18', 'Belum Dikerjakan');

INSERT INTO `notes` (`user_id`, `course_id`, `title`, `content`) VALUES
(1, 1, 'Konsep Dasar MVC', 'MVC (Model-View-Controller) adalah pola arsitektur yang memisahkan aplikasi menjadi tiga komponen utama:\n1. Model: Mengelola data dan logika bisnis.\n2. View: Menampilkan informasi kepada user.\n3. Controller: Menghubungkan Model dan View.'),
(1, 2, 'Normalisasi Database', 'Normalisasi adalah proses menyusun database untuk mengurangi redundansi data dan meningkatkan integritas data. Tahapannya meliputi 1NF, 2NF, dan 3NF.'),
(1, 1, 'Pengenalan JDBC', 'JDBC (Java Database Connectivity) adalah API Java untuk menghubungkan aplikasi Java ke database relasional. Langkah utama:\n1. Register Driver\n2. Buka Koneksi\n3. Buat Statement\n4. Eksekusi Query\n5. Proses ResultSet\n6. Tutup Koneksi');
