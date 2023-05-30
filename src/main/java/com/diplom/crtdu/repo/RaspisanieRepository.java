package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Raspisanie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RaspisanieRepository extends CrudRepository<Raspisanie,Long> {
    List<Raspisanie> findByTeacherId(Long id);
    @Query(value = "SELECT raspisanie.*\n" +
            "FROM raspisanie \n" +
            "LEFT JOIN krujok ON raspisanie.krujok_id = krujok.id \n" +
            "LEFT JOIN kid_krujok ON kid_krujok.krujok_id = krujok.id \n" +
            "LEFT JOIN kid ON kid_krujok.kid_id = kid.id\n" +
            "WHERE kid.id = :id", nativeQuery = true)
    List<Raspisanie> findByKidId(@Param("id") Long id);
}
