package controller;

import service.AIService;

public class AIController {

    private final AIService geminiService;

    public AIController() {
        this.geminiService = new AIService();
    }

    // Constructor to inject custom GeminiService if needed (e.g. for testing)
    public AIController(AIService geminiService) {
        this.geminiService = geminiService;
    }

    public String askAI(String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "Prompt cannot be empty.";
        }
        return geminiService.generateContent(prompt);
    }

    public String generateStudyPlan(java.util.List<model.Course> courses, java.util.List<model.Task> tasks) {
        if ((courses == null || courses.isEmpty()) && (tasks == null || tasks.isEmpty())) {
            return "Belum ada data mata kuliah atau tugas. Silakan tambahkan mata kuliah dan tugas terlebih dahulu.";
        }
        String prompt = geminiService.buildStudyPlanPrompt(courses, tasks);
        return askAI(prompt);
    }

    public String generateAcademicHealthAnalysis(int totalCourses, int totalTasks, long completedTasks, long pendingTasks, int totalNotes) {
        String prompt = geminiService.buildAcademicHealthPrompt(totalCourses, totalTasks, completedTasks, pendingTasks, totalNotes);
        return askAI(prompt);
    }

    public String generateQuiz(String noteTitle, String noteContent) {
        if (noteContent == null || noteContent.trim().isEmpty()) {
            return "Isi catatan kosong. Silakan pilih catatan yang memiliki isi terlebih dahulu.";
        }
        String prompt = geminiService.buildQuizGeneratorPrompt(noteTitle, noteContent);
        return askAI(prompt);
    }
}
