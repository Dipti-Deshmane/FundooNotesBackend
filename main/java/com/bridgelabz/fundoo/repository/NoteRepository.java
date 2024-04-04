package com.bridgelabz.fundoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.model.Note;

import jakarta.transaction.Transactional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query(value = "select * from notes where is_archive = 0 and is_trash = 0", nativeQuery = true)
    List<Note> findAllNotes();

	 @Query("SELECT n FROM Note n WHERE n.title LIKE %:keyword%")
	 List<Note> findByTitle(@Param("keyword") String keyword);
	 
    @Query(value = "select * from notes where is_archive = 0 and is_trash = 0 and user_id = :userId", nativeQuery = true)
    List<Note> findAllNotesByUserId(int userId);

    @Query(value = "select * from notes where is_archive = 1 and is_trash = 0", nativeQuery = true)
    List<Note> findArchiveNotes();
    
    @Query(value = "select * from notes where is_pinned = 1 and is_trash = 0 and is_archive = 0",   nativeQuery = true)
    List<Note> findPinnedNotes();

    @Query(value = "select * from notes where is_archive = 1 and is_trash = 0 and user_id = :userId", nativeQuery = true)
    List<Note> findAllArchiveNotesByUserId(int userId);
    
    @Query(value = "select * from notes where is_pinned = 1 and is_trash = 0 and is_archive = 0 and user_id = :userId", nativeQuery = true)
    List<Note> findAllPinnedNotesByUserId(int userId);

    @Query(value = "select * from notes where is_trash = 1 and is_archive = 0 or is_trash = 1 and is_archive = 1", nativeQuery = true)
    List<Note> findTrashNotes();

    @Query(value = "select * from notes where is_trash = 1 and is_archive = 0 and user_id = :userId", nativeQuery = true)
    List<Note> findAllTrashNotesByUserId(int userId);

    @Transactional
    @Modifying
    @Query(value = "update notes set is_archive = 1 where id = :id", nativeQuery = true)
    void setNoteToArchive(int id);

    @Transactional
    @Modifying
    @Query(value = "update notes set is_Pinned = 1 where id = :id", nativeQuery = true)
    void setNoteToPinned(int id);
    
    @Transactional
    @Modifying
    @Query(value = "update notes set is_archive = 0 where id = :id", nativeQuery = true)
    void setNoteToUnArchive(int id);
    
    @Transactional
    @Modifying
    @Query(value = "update notes set is_Pinned = 0 where id = :id", nativeQuery = true)
    void setNoteToUnPinned(int id);

    @Transactional
    @Modifying
    @Query(value = "update notes set is_trash = 1 where id = :id", nativeQuery = true)
    void setNoteToTrash(int id);

    @Transactional
    @Modifying
    @Query(value = "update notes set is_trash = 0 where id = :id", nativeQuery = true)
    void setNoteToUnTrash(int id);

    @Query(value = "select label_id from notes_label where id = :id", nativeQuery = true)
    List<Integer> findAllLabels(int id);

}
