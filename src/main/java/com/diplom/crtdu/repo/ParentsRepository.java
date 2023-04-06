package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Parents;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ParentsRepository extends CrudRepository<Parents,Long> {

    List<Parents> findByKidsId(Long id);
}
