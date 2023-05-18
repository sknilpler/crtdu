package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Dostijenie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DostijenieRepository extends CrudRepository<Dostijenie,Long> {
    List<Dostijenie> findByMeropriyatieId(Long id);
    List<Dostijenie> findByKidId(Long id);

    @Query(value = "SELECT napravlenie FROM dostijenie GROUP BY napravlenie;", nativeQuery = true)
    List<String> getNapravlenie();
}
