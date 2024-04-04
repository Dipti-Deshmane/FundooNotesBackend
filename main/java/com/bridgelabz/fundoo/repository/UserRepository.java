package com.bridgelabz.fundoo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>  {

	Optional<User> findByEmail(String email);
	Optional<User> findByPassword(String password);
	Optional<User> findByUserId(Integer userId);
	
	@Query(value = "select * from user where email = :email and password = :password", nativeQuery = true)
    User findByEmailIdAndPassword(String email, String password);

}
