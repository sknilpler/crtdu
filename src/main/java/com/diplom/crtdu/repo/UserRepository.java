package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,Long> {
}
