package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Norma;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NormaRepository extends CrudRepository<Norma, Long> {

    List<Norma> findByAgeAndTypeKrujokId(String age, Long id);
}
