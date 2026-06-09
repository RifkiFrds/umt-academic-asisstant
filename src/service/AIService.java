package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import utils.ConfigReader;

public class AIService {

    private final HttpClient httpClient;
    private final Gson gson;

    public AIService() {
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public String generateContent(String prompt) {
        String token = ConfigReader.get("REPLICATE_API_TOKEN");

        if (token == null || token.trim().isEmpty()) {
            return "Error: REPLICATE_API_TOKEN is not configured. Please set it in application.properties.";
        }

        try {
            String url = "https://api.replicate.com/v1/models/google/gemini-2.5-flash/predictions";

            // Build payload matching Replicate schema
            JsonObject inputObj = new JsonObject();
            inputObj.addProperty("prompt", prompt);
            inputObj.addProperty("top_p", 0.95);
            inputObj.addProperty("temperature", 1.0);
            inputObj.addProperty("max_output_tokens", 65535);

            JsonObject payload = new JsonObject();
            payload.add("input", inputObj);

            String requestBody = gson.toJson(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .header("Prefer", "wait")
                    .POST(BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
                
                // Check status
                String status = jsonResponse.has("status") ? jsonResponse.get("status").getAsString() : "";
                if ("failed".equals(status)) {
                    String errorMsg = jsonResponse.has("error") && !jsonResponse.get("error").isJsonNull() 
                            ? jsonResponse.get("error").getAsString() : "Unknown prediction error";
                    return "Error: Replicate prediction failed: " + errorMsg;
                }

                if (jsonResponse.has("output") && !jsonResponse.get("output").isJsonNull()) {
                    JsonElement outputElement = jsonResponse.get("output");
                    if (outputElement.isJsonArray()) {
                        StringBuilder sb = new StringBuilder();
                        for (JsonElement el : outputElement.getAsJsonArray()) {
                            sb.append(el.getAsString());
                        }
                        return sb.toString();
                    } else {
                        return outputElement.getAsString();
                    }
                }
                return "Error: Unexpected API response structure: " + response.body();
            } else {
                return "Error: Replicate API request failed with status code " + statusCode + "\nResponse: " + response.body();
            }
        } catch (Exception e) {
            System.err.println("[GeminiService] Error calling Replicate: " + e.getMessage());
            return "Error calling Replicate API: " + e.getMessage();
        }
    }

    public String buildStudyPlanPrompt(java.util.List<model.Course> courses, java.util.List<model.Task> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Anda adalah seorang asisten produktivitas akademik.\n");
        sb.append("Buatlah rencana belajar mingguan yang dipersonalisasi berdasarkan data berikut:\n\n");
        
        sb.append("Mata Kuliah:\n");
        if (courses == null || courses.isEmpty()) {
            sb.append("- (Belum ada mata kuliah)\n");
        } else {
            for (model.Course c : courses) {
                sb.append(String.format("- %s (%s, SKS: %d, Dosen: %s)\n", c.getCourseName(), c.getCourseCode(), c.getSks(), c.getLecturer()));
            }
        }
        
        sb.append("\nTugas:\n");
        if (tasks == null || tasks.isEmpty()) {
            sb.append("- (Belum ada tugas)\n");
        } else {
            for (model.Task t : tasks) {
                String statusStr = "Belum Dikerjakan";
                if (t.getStatus() == model.TaskStatus.COMPLETED) {
                    statusStr = "Selesai";
                } else if (t.getStatus() == model.TaskStatus.IN_PROGRESS) {
                    statusStr = "Sedang Dikerjakan";
                }
                sb.append(String.format("- %s (Tenggat: %s, Status: %s)\n", t.getTitle(), t.getDeadline().toString(), statusStr));
            }
        }
        
        sb.append("\nPersyaratan:\n");
        sb.append("- Prioritaskan tenggat waktu terdekat\n");
        sb.append("- Seimbangkan beban kerja\n");
        sb.append("- Cegah kelebihan beban pada satu hari\n");
        sb.append("- Gunakan Bahasa Indonesia\n");
        sb.append("- Gunakan jadwal harian yang jelas\n\n");
        
        sb.append("Format keluaran:\n");
        sb.append("Senin\n* ...\n\n");
        sb.append("Selasa\n* ...\n\n");
        sb.append("Rabu\n* ...\n\n");
        sb.append("Kamis\n* ...\n\n");
        sb.append("Jumat\n* ...\n\n");
        sb.append("Sabtu\n* ...\n\n");
        sb.append("Minggu\n* ...\n");
        
        return sb.toString();
    }

    public String buildAcademicHealthPrompt(int totalCourses, int totalTasks, long completedTasks, long pendingTasks, int totalNotes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Anda adalah seorang asisten produktivitas akademik.\n");
        sb.append("Lakukan analisis kesehatan akademik berdasarkan data berikut:\n\n");
        sb.append("Data Akademik:\n");
        sb.append("- Total Mata Kuliah: ").append(totalCourses).append("\n");
        sb.append("- Total Tugas: ").append(totalTasks).append("\n");
        sb.append("- Tugas Selesai: ").append(completedTasks).append("\n");
        sb.append("- Tugas Tertunda: ").append(pendingTasks).append("\n");
        sb.append("- Total Catatan Kuliah: ").append(totalNotes).append("\n\n");
        sb.append("Persyaratan analisis:\n");
        sb.append("1. Berikan skor produktivitas dari skala 0-100.\n");
        sb.append("2. Tentukan kekuatan belajar mahasiswa.\n");
        sb.append("3. Tentukan area perbaikan/kelemahan belajar.\n");
        sb.append("4. Berikan saran/rekomendasi konkret.\n");
        sb.append("5. Gunakan Bahasa Indonesia.\n");
        sb.append("6. JANGAN gunakan simbol markdown seperti *, **, #, atau ## dalam respon Anda.\n\n");
        sb.append("Format keluaran:\n");
        sb.append("ANALISIS AKADEMIK\n\n");
        sb.append("Skor Produktivitas:\n[Skor]/100\n\n");
        sb.append("Kekuatan:\n1. ...\n2. ...\n\n");
        sb.append("Area Perbaikan:\n1. ...\n2. ...\n\n");
        sb.append("Rekomendasi:\n1. ...\n2. ...\n");
        return sb.toString();
    }

    public String buildQuizGeneratorPrompt(String noteTitle, String noteContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Anda adalah seorang asisten pendidik.\n");
        sb.append("Buatlah kuis latihan otomatis dari catatan kuliah berikut:\n\n");
        sb.append("Judul Catatan: ").append(noteTitle).append("\n");
        sb.append("Isi Catatan:\n").append(noteContent).append("\n\n");
        sb.append("Persyaratan kuis:\n");
        sb.append("1. Hasilkan 5 Soal Pilihan Ganda.\n");
        sb.append("2. Setiap soal memiliki 4 opsi pilihan (A, B, C, D).\n");
        sb.append("3. Berikan Kunci Jawaban di setiap akhir soal.\n");
        sb.append("4. Gunakan Bahasa Indonesia.\n");
        sb.append("5. JANGAN gunakan simbol markdown seperti *, **, #, atau ## dalam respon Anda.\n\n");
        sb.append("Format keluaran:\n");
        sb.append("KUIS LATIHAN\n\n");
        sb.append("SOAL 1\n");
        sb.append("Pertanyaan:\n...\n");
        sb.append("A. ...\n");
        sb.append("B. ...\n");
        sb.append("C. ...\n");
        sb.append("D. ...\n");
        sb.append("Jawaban: [Pilihan]\n\n");
        sb.append("SOAL 2\n...\n");
        return sb.toString();
    }
}
