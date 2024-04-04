package com.bridgelabz.fundoo.DTO;

import jakarta.persistence.Column;

public class NoteDTO {
    public String title;
	public String description;

	@Column(name = "is_archive")
    public Boolean isArchive;

    @Column(name = "is_trash")
    public Boolean isTrash;

}
