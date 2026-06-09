package controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.NoteDAO;
import model.Note;

public class NoteController {

    private final NoteDAO noteDAO;

    public NoteController() {
        this.noteDAO = new NoteDAO();
    }

    // Constructor allowing injection
    public NoteController(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public boolean addNote(Note note) {
        validateNote(note);
        try {
            noteDAO.insertNote(note);
            return true;
        } catch (SQLException e) {
            System.err.println("[NoteController] Error adding note: " + e.getMessage());
            return false;
        }
    }

    public boolean updateNote(Note note) {
        validateNote(note);
        try {
            noteDAO.updateNote(note);
            return true;
        } catch (SQLException e) {
            System.err.println("[NoteController] Error updating note: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteNote(int id) {
        try {
            noteDAO.deleteNote(id);
            return true;
        } catch (SQLException e) {
            System.err.println("[NoteController] Error deleting note: " + e.getMessage());
            return false;
        }
    }

    public List<Note> getAllNotes() {
        try {
            return noteDAO.getAllNotes();
        } catch (SQLException e) {
            System.err.println("[NoteController] Error fetching all notes: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Note getNoteById(int id) {
        try {
            return noteDAO.getNoteById(id);
        } catch (SQLException e) {
            System.err.println("[NoteController] Error fetching note by ID: " + e.getMessage());
            return null;
        }
    }

    private void validateNote(Note note) {
        if (note == null) {
            throw new IllegalArgumentException("Note cannot be null.");
        }
        if (note.getTitle() == null || note.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Note title cannot be empty.");
        }
        if (note.getContent() == null || note.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("Note content cannot be empty.");
        }
    }
}
