package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Kid;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KidRepository extends CrudRepository<Kid,Long> {

    List<Kid> findByParentsId(Long id);
}
