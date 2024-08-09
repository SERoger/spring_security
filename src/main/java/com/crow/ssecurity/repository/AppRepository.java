package com.crow.ssecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.crow.ssecurity.entity.AppUser;

@Repository
public interface AppRepository extends JpaRepository<AppUser, Integer>{
	Optional<AppUser> findByUserName(String userName);
}
