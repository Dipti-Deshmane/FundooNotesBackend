package com.bridgelabz.fundoo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "label_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Label {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int labelId;
    private String labelName;

    @ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<Note> notesModelList;

	public Label(String labelName) {
	   this.labelName = labelName;
	}

	public Label(String labelName, User user) {
	   this.user = user;
	   this.labelName = labelName;
	}
	        
	public Label() {
		super();
	}

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
	return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Note> getNotesModelList() {
		return notesModelList;
	}

	public void setNotesModelList(List<Note> notesModelList) {
		this.notesModelList = notesModelList;
	}
}
