-- Advanced Authentication migration script
USE umt_academic_assistant;

-- 1. Create users table if not exists
CREATE TABLE IF NOT EXISTS `users` (
    `id` INT AUTO_INCREMENT PRIMARY KEY,
    `full_name` VARCHAR(100) NOT NULL,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. Populate default admin if table is empty
INSERT IGNORE INTO `users` (`id`, `full_name`, `username`, `password`) VALUES
(1, 'Administrator', 'admin', 'admin123');

-- 3. Alter courses table
ALTER TABLE `courses` ADD COLUMN `user_id` INT DEFAULT 1;
ALTER TABLE `courses` ADD CONSTRAINT `fk_courses_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

-- 4. Alter tasks table
ALTER TABLE `tasks` ADD COLUMN `user_id` INT DEFAULT 1;
ALTER TABLE `tasks` ADD CONSTRAINT `fk_tasks_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

-- 5. Alter notes table
ALTER TABLE `notes` ADD COLUMN `user_id` INT DEFAULT 1;
ALTER TABLE `notes` ADD CONSTRAINT `fk_notes_users` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
