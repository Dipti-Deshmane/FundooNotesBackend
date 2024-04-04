package com.bridgelabz.fundoo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name= "notes")
public class Note {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	private String title;
	private String description;
	
	@Column(name = "is_archive")
	private boolean isArchive;
	
	@Column(name = "is_trash")
	private boolean isTrash;

	@Column(name = "is_pinned")
	private boolean isPinned;

	@ManyToOne
    @JoinColumn(name = "user_id")
	@JsonIgnore
    private User user;
	
	@ManyToMany
	@JoinTable(name = "notes_label",
	joinColumns = @JoinColumn(name = "id"),
	inverseJoinColumns = @JoinColumn(name = "label_id")
	)
	private List<Label> labelModelList;
	
	public Note(Note notesModel, List<Label> labelModelList) {
        this.id = notesModel.id;
        this.title = notesModel.title;
        this.description = notesModel.description;
        this.isArchive = notesModel.isArchive;
        this.isTrash = notesModel.isTrash;
        this.labelModelList = labelModelList;
        this.isPinned=notesModel.isPinned;
        this.user = notesModel.user;
    }
	
	public Note(String title, String description, boolean isArchive, boolean isTrash, boolean isPinned,List<Label> labelModelList) {
		super();
		this.title = title;
		this.description = description;	
		this.isArchive = isArchive;
		this.isTrash = isTrash;
		this.isPinned=isPinned;
		this.labelModelList = labelModelList;
	}
	
	public Note() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	public boolean isTrash() {
		return isTrash;
	}

	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public List<Label> getLabelModelList() {
		return labelModelList;
	}

	public void setLabelModelList(List<Label> labelModelList) {
		this.labelModelList = labelModelList;
	}	
	
}
