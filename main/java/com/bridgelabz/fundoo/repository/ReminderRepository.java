package com.bridgelabz.fundoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.model.Reminder;

import jakarta.transaction.Transactional;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Integer> {
	@Transactional
    @Modifying
    @Query(value = "delete from reminder_data where id = :id and reminder_id = :reminderId", nativeQuery = true)
    void deleteByNotesId(int id, int reminderId);

    @Transactional
    @Modifying
    @Query(value = "delete from reminder_data where id = :id", nativeQuery = true)
    void deleteById(int id);

    @Query(value = "select * from reminder_data where id = :id", nativeQuery = true)
    Reminder findByNotes(int id);

	Reminder save(Reminder reminder);
}
