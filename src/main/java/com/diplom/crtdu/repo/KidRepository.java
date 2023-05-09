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

    List<Kid> findByGrazhdanstvo(String gr);
    List<Kid> findBySex(Boolean sex);

    @Query(value = "SELECT ca.name AS 'creative_association', CONCAT(ca.pdo_surname, ' ', LEFT(ca.pdo_name, 1), '.', LEFT(ca.pdo_patronymic, 1), '.') AS full_name, COUNT(*) AS 'total_kids',\n" +
            "       SUM(CASE WHEN kid.sex = 1 THEN 1 ELSE 0 END) AS 'boys',\n" +
            "       SUM(CASE WHEN kid.sex = 0 THEN 1 ELSE 0 END) AS 'girls',\n" +
            "       SUM(CASE WHEN kid.grazhdanstvo = 'РФ' THEN 1 ELSE 0 END) AS 'RF',\n" +
            "       SUM(CASE WHEN kid.grazhdanstvo = 'РК' THEN 1 ELSE 0 END) AS 'RK',\n" +
            "       SUM(CASE WHEN kid.grazhdanstvo = 'Другое' THEN 1 ELSE 0 END) AS 'other'\n" +
            "FROM creative_association ca\n" +
            "JOIN krujok k ON ca.id = k.creative_association_id\n" +
            "JOIN kid_krujok kk ON k.id = kk.krujok_id\n" +
            "JOIN kid kid ON kk.kid_id = kid.id\n" +
            "WHERE k.archive = 0\n" +
            "GROUP BY ca.name, ca.pdo_surname, ca.pdo_name, ca.pdo_patronymic;", nativeQuery = true)
    List<Object[]> getStatByKids();
}
