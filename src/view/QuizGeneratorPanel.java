package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import controller.AIController;
import controller.NoteController;
import model.Note;

public class QuizGeneratorPanel extends JPanel {

    private final AIController aiController;
    private final NoteController noteController;

    private CardLayout cardLayout;
    private JPanel pnlCards;

    // Card 1: INITIAL STATE
    private JComboBox<NoteWrapper> cbNotes;
    private JButton btnStartQuiz;

    // Card 2: LOADING STATE
    private JLabel lblLoading;
    private JProgressBar progressBar;

    // Card 3: QUIZ STATE
    private JLabel lblQuizProgress;
    private JTextArea txtQuestion;
    private JRadioButton rbA;
    private JRadioButton rbB;
    private JRadioButton rbC;
    private JRadioButton rbD;
    private ButtonGroup btnGroupOptions;
    private JButton btnPrev;
    private JButton btnNext;
    private JButton btnFinish;

    // Card 4: RESULT STATE
    private JLabel lblScore;
    private JLabel lblCorrectCount;
    private JLabel lblFeedback;
    private JPanel pnlReviewContainer;
    private JButton btnRestart;

    // State Variables
    private List<QuizQuestion> quizQuestions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    public QuizGeneratorPanel() {
        this.aiController = new AIController();
        this.noteController = new NoteController();
        initComponents();
        loadNotes();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        pnlCards.setOpaque(false);

        // ─── CARD 1: INITIAL STATE ────────────────────────────────
        JPanel pnlInitial = new JPanel(new GridBagLayout());
        pnlInitial.setBackground(Color.WHITE);
        pnlInitial.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblInitIcon = new JLabel("📝", SwingConstants.CENTER);
        lblInitIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        pnlInitial.add(lblInitIcon, gbc);

        JLabel lblInitTitle = new JLabel("Quiz Generator 2.0", SwingConstants.CENTER);
        lblInitTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblInitTitle.setForeground(new Color(17, 24, 39));
        pnlInitial.add(lblInitTitle, gbc);

        JTextArea txtInitDesc = new JTextArea("Uji pemahaman materi Anda dengan kuis interaktif yang dibuat otomatis dari catatan Anda. Pilih catatan di bawah ini untuk memulai.");
        txtInitDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtInitDesc.setForeground(Color.GRAY);
        txtInitDesc.setLineWrap(true);
        txtInitDesc.setWrapStyleWord(true);
        txtInitDesc.setEditable(false);
        txtInitDesc.setFocusable(false);
        txtInitDesc.setBackground(Color.WHITE);
        txtInitDesc.setColumns(30);
        txtInitDesc.setAlignmentX(CENTER_ALIGNMENT);
        pnlInitial.add(txtInitDesc, gbc);

        // Dropdown Container
        JPanel pnlDropdown = new JPanel(new BorderLayout(5, 5));
        pnlDropdown.setOpaque(false);
        JLabel lblDropdown = new JLabel("Pilih Catatan Kuliah:");
        lblDropdown.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDropdown.setForeground(new Color(75, 85, 99));
        cbNotes = new JComboBox<>();
        cbNotes.setPreferredSize(new Dimension(320, 38));
        pnlDropdown.add(lblDropdown, BorderLayout.NORTH);
        pnlDropdown.add(cbNotes, BorderLayout.CENTER);
        pnlInitial.add(pnlDropdown, gbc);

        btnStartQuiz = new JButton("Mulai Kuis");
        btnStartQuiz.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnStartQuiz.setForeground(Color.WHITE);
        btnStartQuiz.setBackground(new Color(139, 92, 246)); // Purple
        btnStartQuiz.setPreferredSize(new Dimension(240, 45));
        btnStartQuiz.setFocusPainted(false);
        btnStartQuiz.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnStartQuiz.addActionListener(e -> onStartQuiz());

        JPanel pnlBtnContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlBtnContainer.setOpaque(false);
        pnlBtnContainer.add(btnStartQuiz);
        pnlInitial.add(pnlBtnContainer, gbc);

        // ─── CARD 2: LOADING STATE ────────────────────────────────
        JPanel pnlLoading = new JPanel(new GridBagLayout());
        pnlLoading.setBackground(Color.WHITE);

        lblLoading = new JLabel("Membuat Kuis Latihan dari Catatan Anda...", SwingConstants.CENTER);
        lblLoading.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblLoading.setForeground(new Color(75, 85, 99));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(300, 8));
        progressBar.setForeground(new Color(139, 92, 246));
        progressBar.setBackground(new Color(243, 244, 246));
        progressBar.setBorder(BorderFactory.createEmptyBorder());

        GridBagConstraints gbcL = new GridBagConstraints();
        gbcL.gridx = 0;
        gbcL.gridy = GridBagConstraints.RELATIVE;
        gbcL.insets = new Insets(15, 0, 15, 0);

        pnlLoading.add(lblLoading, gbcL);
        pnlLoading.add(progressBar, gbcL);

        // ─── CARD 3: QUIZ INTERACTIVE VIEW ────────────────────────
        JPanel pnlQuiz = new JPanel(new BorderLayout(15, 15));
        pnlQuiz.setBackground(Color.WHITE);
        pnlQuiz.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Question Container Card
        JPanel cardQuestion = new JPanel(new BorderLayout(0, 15));
        cardQuestion.setBackground(Color.WHITE);
        cardQuestion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Quiz Progress Header
        lblQuizProgress = new JLabel("Pertanyaan 1 dari 5");
        lblQuizProgress.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblQuizProgress.setForeground(new Color(139, 92, 246));
        cardQuestion.add(lblQuizProgress, BorderLayout.NORTH);

        // Question Area
        txtQuestion = new JTextArea();
        txtQuestion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        txtQuestion.setForeground(new Color(17, 24, 39));
        txtQuestion.setEditable(false);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        txtQuestion.setBackground(Color.WHITE);
        txtQuestion.setFocusable(false);

        JScrollPane scrollQuestion = new JScrollPane(txtQuestion);
        scrollQuestion.setBorder(null);
        scrollQuestion.setPreferredSize(new Dimension(0, 80));
        cardQuestion.add(scrollQuestion, BorderLayout.CENTER);

        // Options Selection Panel
        JPanel pnlOptions = new JPanel(new GridLayout(4, 1, 0, 12));
        pnlOptions.setOpaque(false);

        rbA = new JRadioButton("A. Pilihan A");
        rbB = new JRadioButton("B. Pilihan B");
        rbC = new JRadioButton("C. Pilihan C");
        rbD = new JRadioButton("D. Pilihan D");

        Font optFont = new Font("Segoe UI", Font.PLAIN, 13);
        Color optColor = new Color(55, 65, 81);

        for (JRadioButton rb : new JRadioButton[]{rbA, rbB, rbC, rbD}) {
            rb.setFont(optFont);
            rb.setForeground(optColor);
            rb.setOpaque(false);
            rb.setFocusPainted(false);
            pnlOptions.add(rb);
        }

        btnGroupOptions = new ButtonGroup();
        btnGroupOptions.add(rbA);
        btnGroupOptions.add(rbB);
        btnGroupOptions.add(rbC);
        btnGroupOptions.add(rbD);

        cardQuestion.add(pnlOptions, BorderLayout.SOUTH);
        pnlQuiz.add(cardQuestion, BorderLayout.CENTER);

        // Navigation Footer Panel
        JPanel pnlQuizFooter = new JPanel(new BorderLayout());
        pnlQuizFooter.setOpaque(false);

        JPanel pnlNavButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlNavButtons.setOpaque(false);

        btnPrev = new JButton("Sebelumnya");
        btnPrev.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPrev.setForeground(new Color(75, 85, 99));
        btnPrev.setBackground(new Color(243, 244, 246));
        btnPrev.setPreferredSize(new Dimension(120, 38));
        btnPrev.setFocusPainted(false);
        btnPrev.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnPrev.addActionListener(e -> navigateQuestion(-1));

        btnNext = new JButton("Berikutnya");
        btnNext.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnNext.setForeground(Color.WHITE);
        btnNext.setBackground(new Color(139, 92, 246));
        btnNext.setPreferredSize(new Dimension(120, 38));
        btnNext.setFocusPainted(false);
        btnNext.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNext.addActionListener(e -> navigateQuestion(1));

        pnlNavButtons.add(btnPrev);
        pnlNavButtons.add(btnNext);

        btnFinish = new JButton("Selesai Kuis");
        btnFinish.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFinish.setForeground(Color.WHITE);
        btnFinish.setBackground(new Color(16, 185, 129)); // Emerald Green
        btnFinish.setPreferredSize(new Dimension(130, 38));
        btnFinish.setFocusPainted(false);
        btnFinish.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFinish.addActionListener(e -> onFinishQuiz());

        pnlQuizFooter.add(pnlNavButtons, BorderLayout.WEST);
        pnlQuizFooter.add(btnFinish, BorderLayout.EAST);
        pnlQuiz.add(pnlQuizFooter, BorderLayout.SOUTH);

        // ─── CARD 4: RESULT SCREEN ────────────────────────────────
        JPanel pnlResult = new JPanel(new BorderLayout(15, 15));
        pnlResult.setBackground(Color.WHITE);
        pnlResult.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel cardResult = new JPanel(new GridBagLayout());
        cardResult.setBackground(Color.WHITE);
        cardResult.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbcR = new GridBagConstraints();
        gbcR.gridx = 0;
        gbcR.gridy = GridBagConstraints.RELATIVE;
        gbcR.insets = new Insets(8, 0, 8, 0);
        gbcR.fill = GridBagConstraints.HORIZONTAL;
        gbcR.anchor = GridBagConstraints.CENTER;

        JLabel lblResTitle = new JLabel("Hasil Kuis Latihan", SwingConstants.CENTER);
        lblResTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblResTitle.setForeground(new Color(17, 24, 39));
        cardResult.add(lblResTitle, gbcR);

        lblScore = new JLabel("Nilai: 0 / 100", SwingConstants.CENTER);
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblScore.setForeground(new Color(139, 92, 246));
        cardResult.add(lblScore, gbcR);

        lblCorrectCount = new JLabel("Jawaban Benar: 0 dari 0", SwingConstants.CENTER);
        lblCorrectCount.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblCorrectCount.setForeground(Color.GRAY);
        cardResult.add(lblCorrectCount, gbcR);

        lblFeedback = new JLabel("Sangat Baik!", SwingConstants.CENTER);
        lblFeedback.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblFeedback.setForeground(new Color(16, 185, 129));
        cardResult.add(lblFeedback, gbcR);

        // Review Scroll Area
        pnlReviewContainer = new JPanel();
        pnlReviewContainer.setLayout(new BoxLayout(pnlReviewContainer, BoxLayout.Y_AXIS));
        pnlReviewContainer.setBackground(Color.WHITE);

        JScrollPane scrollReview = new JScrollPane(pnlReviewContainer);
        scrollReview.setPreferredSize(new Dimension(450, 200));
        scrollReview.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1),
                "Review Jawaban", 0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(75, 85, 99)
        ));
        cardResult.add(scrollReview, gbcR);

        btnRestart = new JButton("Kuis Baru");
        btnRestart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRestart.setForeground(Color.WHITE);
        btnRestart.setBackground(new Color(139, 92, 246));
        btnRestart.setPreferredSize(new Dimension(150, 38));
        btnRestart.setFocusPainted(false);
        btnRestart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRestart.addActionListener(e -> {
            cardLayout.show(pnlCards, "INITIAL");
        });
        
        JPanel pnlRestartContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlRestartContainer.setOpaque(false);
        pnlRestartContainer.add(btnRestart);
        cardResult.add(pnlRestartContainer, gbcR);

        pnlResult.add(cardResult, BorderLayout.CENTER);

        // Add cards to holder
        pnlCards.add(pnlInitial, "INITIAL");
        pnlCards.add(pnlLoading, "LOADING");
        pnlCards.add(pnlQuiz, "QUIZ");
        pnlCards.add(pnlResult, "RESULT");

        add(pnlCards, BorderLayout.CENTER);
        cardLayout.show(pnlCards, "INITIAL");

        // Hook up option selections to save answers immediately
        for (JRadioButton rb : new JRadioButton[]{rbA, rbB, rbC, rbD}) {
            rb.addActionListener(e -> saveSelectedAnswer());
        }
    }

    public void loadNotes() {
        cbNotes.removeAllItems();
        List<Note> notes = noteController.getAllNotes();
        for (Note n : notes) {
            cbNotes.addItem(new NoteWrapper(n));
        }
    }

    private void onStartQuiz() {
        NoteWrapper wrapper = (NoteWrapper) cbNotes.getSelectedItem();
        if (wrapper == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih catatan terlebih dahulu.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Note note = wrapper.getNote();
        if (note.getContent() == null || note.getContent().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Catatan terpilih kosong. Silakan pilih catatan yang memiliki isi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cardLayout.show(pnlCards, "LOADING");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return aiController.generateQuiz(note.getTitle(), note.getContent());
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    quizQuestions = parseQuizQuestions(result);
                    if (quizQuestions.isEmpty()) {
                        throw new Exception("Format kuis tidak valid atau gagal diproses oleh AI.");
                    }
                    currentQuestionIndex = 0;
                    displayQuestion(currentQuestionIndex);
                    cardLayout.show(pnlCards, "QUIZ");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(QuizGeneratorPanel.this,
                            "Gagal membuat kuis: " + ex.getMessage(),
                            "Kesalahan", JOptionPane.ERROR_MESSAGE);
                    cardLayout.show(pnlCards, "INITIAL");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        };
        worker.execute();
    }

    private List<QuizQuestion> parseQuizQuestions(String text) {
        List<QuizQuestion> questions = new ArrayList<>();
        if (text == null) return questions;

        String[] lines = text.split("\n");
        QuizQuestion currentQ = null;
        String currentSection = ""; // "Q", "A", "B", "C", "D"

        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            String lower = trimmed.toLowerCase();
            if (lower.startsWith("soal ")) {
                if (currentQ != null && isValidQuestion(currentQ)) {
                    questions.add(currentQ);
                }
                currentQ = new QuizQuestion();
                currentSection = "";
                continue;
            }

            if (lower.startsWith("pertanyaan:")) {
                currentSection = "Q";
                if (currentQ == null) currentQ = new QuizQuestion();
                String qVal = trimmed.substring("pertanyaan:".length()).trim();
                currentQ.questionText = qVal;
                continue;
            }

            if (trimmed.startsWith("A.") || trimmed.startsWith("a.")) {
                currentSection = "A";
                if (currentQ == null) currentQ = new QuizQuestion();
                currentQ.optionA = trimmed.substring(2).trim();
                continue;
            }
            if (trimmed.startsWith("B.") || trimmed.startsWith("b.")) {
                currentSection = "B";
                if (currentQ == null) currentQ = new QuizQuestion();
                currentQ.optionB = trimmed.substring(2).trim();
                continue;
            }
            if (trimmed.startsWith("C.") || trimmed.startsWith("c.")) {
                currentSection = "C";
                if (currentQ == null) currentQ = new QuizQuestion();
                currentQ.optionC = trimmed.substring(2).trim();
                continue;
            }
            if (trimmed.startsWith("D.") || trimmed.startsWith("d.")) {
                currentSection = "D";
                if (currentQ == null) currentQ = new QuizQuestion();
                currentQ.optionD = trimmed.substring(2).trim();
                continue;
            }

            if (lower.startsWith("jawaban:")) {
                currentSection = "";
                if (currentQ == null) currentQ = new QuizQuestion();
                String ans = trimmed.substring("jawaban:".length()).trim();
                if (!ans.isEmpty()) {
                    currentQ.correctAnswer = ans.substring(0, 1).toUpperCase();
                }
                continue;
            }

            // Append extra lines
            if (currentQ != null && !currentSection.isEmpty()) {
                if (currentSection.equals("Q")) {
                    currentQ.questionText += " " + trimmed;
                } else if (currentSection.equals("A")) {
                    currentQ.optionA += " " + trimmed;
                } else if (currentSection.equals("B")) {
                    currentQ.optionB += " " + trimmed;
                } else if (currentSection.equals("C")) {
                    currentQ.optionC += " " + trimmed;
                } else if (currentSection.equals("D")) {
                    currentQ.optionD += " " + trimmed;
                }
            }
        }

        if (currentQ != null && isValidQuestion(currentQ)) {
            questions.add(currentQ);
        }

        return questions;
    }

    private boolean isValidQuestion(QuizQuestion q) {
        return q.questionText != null && !q.questionText.trim().isEmpty() &&
               q.optionA != null && !q.optionA.trim().isEmpty() &&
               q.correctAnswer != null && !q.correctAnswer.trim().isEmpty();
    }

    private void displayQuestion(int index) {
        if (index < 0 || index >= quizQuestions.size()) return;

        QuizQuestion q = quizQuestions.get(index);
        lblQuizProgress.setText("Pertanyaan " + (index + 1) + " dari " + quizQuestions.size());
        txtQuestion.setText(q.questionText);

        rbA.setText("A. " + q.optionA);
        rbB.setText("B. " + q.optionB);
        rbC.setText("C. " + q.optionC);
        rbD.setText("D. " + q.optionD);

        // Load previous answer
        btnGroupOptions.clearSelection();
        if ("A".equals(q.userAnswer)) rbA.setSelected(true);
        else if ("B".equals(q.userAnswer)) rbB.setSelected(true);
        else if ("C".equals(q.userAnswer)) rbC.setSelected(true);
        else if ("D".equals(q.userAnswer)) rbD.setSelected(true);

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < quizQuestions.size() - 1);
        btnFinish.setVisible(index == quizQuestions.size() - 1);
    }

    private void saveSelectedAnswer() {
        if (quizQuestions.isEmpty()) return;
        QuizQuestion q = quizQuestions.get(currentQuestionIndex);
        if (rbA.isSelected()) q.userAnswer = "A";
        else if (rbB.isSelected()) q.userAnswer = "B";
        else if (rbC.isSelected()) q.userAnswer = "C";
        else if (rbD.isSelected()) q.userAnswer = "D";
    }

    private void navigateQuestion(int direction) {
        saveSelectedAnswer();
        currentQuestionIndex += direction;
        displayQuestion(currentQuestionIndex);
    }

    private void onFinishQuiz() {
        saveSelectedAnswer();
        
        int correctCount = 0;
        for (QuizQuestion q : quizQuestions) {
            if (q.correctAnswer.equalsIgnoreCase(q.userAnswer)) {
                correctCount++;
            }
        }

        int score = (int) (((double) correctCount / quizQuestions.size()) * 100);

        lblScore.setText("Nilai: " + score + " / 100");
        lblCorrectCount.setText("Jawaban Benar: " + correctCount + " dari " + quizQuestions.size());

        if (score == 100) {
            lblFeedback.setText("Sangat Baik! Pemahaman Anda luar biasa.");
            lblFeedback.setForeground(new Color(16, 185, 129));
        } else if (score >= 75) {
            lblFeedback.setText("Baik! Tingkatkan terus belajar Anda.");
            lblFeedback.setForeground(new Color(139, 92, 246));
        } else {
            lblFeedback.setText("Perlu Belajar Lagi. Baca kembali catatan kuliah Anda.");
            lblFeedback.setForeground(new Color(220, 38, 38));
        }

        // Build Review Panel List
        pnlReviewContainer.removeAll();
        for (int i = 0; i < quizQuestions.size(); i++) {
            QuizQuestion q = quizQuestions.get(i);
            boolean isCorrect = q.correctAnswer.equalsIgnoreCase(q.userAnswer);

            JPanel pnlItem = new JPanel(new BorderLayout(5, 5));
            pnlItem.setBackground(Color.WHITE);
            pnlItem.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(229, 231, 235)),
                    new EmptyBorder(8, 8, 8, 8)
            ));

            JLabel lblQTitle = new JLabel("Soal " + (i + 1) + ": " + q.questionText);
            lblQTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblQTitle.setForeground(new Color(17, 24, 39));
            pnlItem.add(lblQTitle, BorderLayout.NORTH);

            JLabel lblStatus = new JLabel(
                    "Pilihan Anda: " + (q.userAnswer.isEmpty() ? "-" : q.userAnswer) + 
                    " | Jawaban Benar: " + q.correctAnswer
            );
            lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            if (isCorrect) {
                lblStatus.setForeground(new Color(5, 150, 105)); // Green
            } else {
                lblStatus.setForeground(new Color(220, 38, 38)); // Red
            }
            pnlItem.add(lblStatus, BorderLayout.SOUTH);

            pnlReviewContainer.add(pnlItem);
        }

        pnlReviewContainer.revalidate();
        pnlReviewContainer.repaint();

        cardLayout.show(pnlCards, "RESULT");
    }

    private static class QuizQuestion {
        public String questionText = "";
        public String optionA = "";
        public String optionB = "";
        public String optionC = "";
        public String optionD = "";
        public String correctAnswer = "";
        public String userAnswer = "";
    }

    private static class NoteWrapper {
        private final Note note;

        public NoteWrapper(Note note) {
            this.note = note;
        }

        public Note getNote() {
            return note;
        }

        @Override
        public String toString() {
            return note.getTitle();
        }
    }
}
