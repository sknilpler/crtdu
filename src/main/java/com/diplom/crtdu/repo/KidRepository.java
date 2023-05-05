package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Kid;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KidRepository extends CrudRepository<Kid,Long> {

    List<Kid> findByParentsId(Long id);

    @Query(value = "SELECT kid.* FROM kid , kid_krujok\n" +
            "WHERE kid.id = kid_krujok.kid_id and kid_krujok.krujok_id = :id\n" +
            "ORDER by kid.surname",  nativeQuery = true)
    List<Kid> findByKrujokOrderBySurname(@Param("id") Long id);

    @Query(value = "SELECT k.*\n" +
            "FROM kid k\n" +
            "JOIN uchastnik u ON k.id = u.kid_id\n" +
            "WHERE u.krujok_id = :id1\n" +
            "AND NOT EXISTS (\n" +
            "  SELECT *\n" +
            "  FROM uchastnik u2\n" +
            "  WHERE u2.kid_id = k.id\n" +
            "  AND u2.meropriyatie_id = :id2\n" +
            ") \n" +
            "ORDER BY k.rang, k.surname", nativeQuery = true)
    List<Kid> findNotIncludedByKrujokAndMeripriyatie(@Param("id1") Long id1, @Param("id2") Long id2);
}
