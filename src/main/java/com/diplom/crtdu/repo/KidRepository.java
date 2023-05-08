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
            "JOIN kid_krujok kk ON k.id = kk.kid_id\n" +
            "WHERE kk.krujok_id = :id1\n" +
            "AND NOT EXISTS (\n" +
            "  SELECT 1\n" +
            "  FROM uchastnik u\n" +
            "  WHERE u.kid_id = k.id\n" +
            "  AND u.krujok_id = kk.krujok_id\n" +
            "  AND u.meropriyatie_id = :id2\n" +
            ")" +
            "ORDER BY k.rang, k.surname", nativeQuery = true)
    List<Kid> findNotIncludedByKrujokAndMeripriyatie(@Param("id1") Long id1, @Param("id2") Long id2);
}
