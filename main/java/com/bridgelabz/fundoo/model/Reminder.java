package com.bridgelabz.fundoo.model;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reminder_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reminderId;

    private LocalDateTime reminder;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Note notesModel;

    public Reminder(Note notesModel, LocalDateTime reminder) {
        this.notesModel = notesModel;
        this.reminder = reminder;
    }

	public Reminder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getReminderId() {
		return reminderId;
	}

	public void setReminderId(int reminderId) {
		this.reminderId = reminderId;
	}

	public LocalDateTime getReminder() {
		return reminder;
	}

	public void setReminder(LocalDateTime reminder) {
		this.reminder = reminder;
	}

	public Note getNotesModel() {
		return notesModel;
	}

	public void setNotesModel(Note notesModel) {
		this.notesModel = notesModel;
	}
    
}
