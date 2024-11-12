package com.snipper.snippets.repository;

import com.snipper.snippets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
