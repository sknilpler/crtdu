package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Ocenka;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OcenkaRepository extends CrudRepository<Ocenka, Long> {

    List<Ocenka> findByKidIdAndKrujokId(Long idKid, Long idKruj);
}
