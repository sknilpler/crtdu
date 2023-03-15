package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
