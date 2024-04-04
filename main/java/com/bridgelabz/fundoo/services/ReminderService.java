package com.bridgelabz.fundoo.services;

import com.bridgelabz.fundoo.model.Note;
import com.bridgelabz.fundoo.model.Reminder;
import com.bridgelabz.fundoo.repository.NoteRepository;
import com.bridgelabz.fundoo.repository.ReminderRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private NoteRepository noteRepository;

    public Reminder setReminder(int id, Reminder reminder) {
        // Retrieve the note by ID
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));

        // Associate the reminder with the note
        reminder.setNotesModel(note);
        return reminderRepository.save(reminder);
    }

    public Reminder updateReminder(int reminderId, Reminder updatedReminder) {
        Reminder existingReminder = reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));

        // Update reminder properties
        existingReminder.setReminder(updatedReminder.getReminder());
        return reminderRepository.save(existingReminder);
    }

    public void deleteReminder(int reminderId) {
        reminderRepository.deleteById(reminderId);
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public Reminder getReminderById(int reminderId) {
        return reminderRepository.findById(reminderId)
                .orElseThrow(() -> new RuntimeException("Reminder not found"));
    }
}
