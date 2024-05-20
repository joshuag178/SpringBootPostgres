package com.SpringBootPSQL.SpringBootPostgres.data.repository;

import com.SpringBootPSQL.SpringBootPostgres.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
