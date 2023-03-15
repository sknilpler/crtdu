package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends JpaRepository<Role,Long> {
    /**
     * Найти роль пользователя по имени
     *
     * @param name имя роли
     * @return роль
     */
    Role findByName(String name);
}
