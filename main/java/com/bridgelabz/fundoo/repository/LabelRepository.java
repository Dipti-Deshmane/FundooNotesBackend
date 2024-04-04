package com.bridgelabz.fundoo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.model.Label;

@Repository
public interface LabelRepository extends JpaRepository<Label, Integer>{
	Label findByLabelName(String labelName);
	
	@Query(value = "select * from label_data where user_id = :userId", nativeQuery = true)
	List<Label> findAllLabelsByUserId(int userId);

	@Query(value = "SELECT * FROM notes_label where label_id = :labelId", nativeQuery = true)
	List<Integer> findAllNotesIdByLabelId(int labelId);

	@Query(value = "select * from label_data where user_id = :userId and label_id = :labelId", nativeQuery = true)
	Label findLabelByUserId(int userId, int labelId);
}
