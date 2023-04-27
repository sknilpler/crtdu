package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Zanyatie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ZanyatieRepository extends CrudRepository<Zanyatie,Long> {

    List<Zanyatie> findByKrujokId(Long id);
}
