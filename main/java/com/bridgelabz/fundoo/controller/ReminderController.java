package com.bridgelabz.fundoo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.model.Reminder;
import com.bridgelabz.fundoo.services.ReminderService;

@RestController
@RequestMapping("/reminders")
public class ReminderController {
	
    @Autowired
    private ReminderService reminderService;

    @PostMapping("/set/{id}")
    public ResponseEntity<Reminder> setReminder(@PathVariable int id, @RequestBody Reminder reminder) {
        Reminder savedReminder = reminderService.setReminder(id, reminder);
        return new ResponseEntity<>(savedReminder, HttpStatus.OK);
    }
    
    @PutMapping("/update/{reminderId}")
    public ResponseEntity<Reminder> updateReminder(@PathVariable int reminderId, @RequestBody Reminder reminder) {
        Reminder updatedReminder = reminderService.updateReminder(reminderId, reminder);
        return new ResponseEntity<>(updatedReminder, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{reminderId}")
    public ResponseEntity<Void> deleteReminder(@PathVariable int reminderId) {
        reminderService.deleteReminder(reminderId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Reminder>> getAllReminders() {
        List<Reminder> reminders = reminderService.getAllReminders();
        return new ResponseEntity<>(reminders, HttpStatus.OK);
    }

    @GetMapping("/{reminderId}")
    public ResponseEntity<Reminder> getReminderById(@PathVariable int reminderId) {
        Reminder reminder = reminderService.getReminderById(reminderId);
        return new ResponseEntity<>(reminder, HttpStatus.OK);
    }
}
