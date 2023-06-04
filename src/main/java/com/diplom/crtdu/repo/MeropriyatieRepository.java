package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Meropriyatie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MeropriyatieRepository extends CrudRepository<Meropriyatie, Long> {

    @Query(value = "SELECT meropriyatie.*\n" +
            "FROM meropriyatie\n" +
            "WHERE meropriyatie.data BETWEEN :d1 AND :d2 ORDER BY meropriyatie.data", nativeQuery = true)
    List<Meropriyatie> findByData(@Param("d1") String date1,
                                  @Param("d2") String date2);

    @Query(value = "SELECT meropriyatie.*\n" +
            "FROM meropriyatie\n" +
            "WHERE meropriyatie.data BETWEEN :d1 AND :d2 ORDER BY meropriyatie.data DESC", nativeQuery = true)
    List<Meropriyatie> findByDataDesc(@Param("d1") String date1,
                                      @Param("d2") String date2);

    @Query(value = "SELECT meropriyatie.*\n" +
            "FROM meropriyatie\n" +
            "WHERE\n" +
            "    meropriyatie.data BETWEEN :d1 AND :d2 AND\n" +
            "    meropriyatie.level_id = :l AND meropriyatie.type_id = :t\n" +
            "ORDER BY\n" +
            "    meropriyatie.data", nativeQuery = true)
    List<Meropriyatie> findByDataAndLevelAndType(@Param("d1") String date1,
                                                 @Param("d2") String date2,
                                                 @Param("l") Long level,
                                                 @Param("t") Long type);

    @Query(value = "SELECT meropriyatie.*\n" +
            "FROM meropriyatie\n" +
            "WHERE\n" +
            "    meropriyatie.data BETWEEN :d1 AND :d2 AND\n" +
            "    meropriyatie.level_id = :l\n" +
            "ORDER BY\n" +
            "    meropriyatie.data", nativeQuery = true)
    List<Meropriyatie> findByDataAndLevel(@Param("d1") String date1,
                                          @Param("d2") String date2,
                                          @Param("l") Long level);

    @Query(value = "SELECT meropriyatie.*\n" +
            "FROM meropriyatie\n" +
            "WHERE\n" +
            "    meropriyatie.data BETWEEN :d1 AND :d2 AND\n" +
            "    meropriyatie.type_id = :t\n" +
            "ORDER BY\n" +
            "    meropriyatie.data", nativeQuery = true)
    List<Meropriyatie> findByDataAndType(@Param("d1") String date1,
                                         @Param("d2") String date2,
                                         @Param("t") Long type);


    @Query(value = "SELECT\n" +
            "    m.id AS id,\n" +
            "    m.name AS name_merop,\n" +
            "    m.data AS date_merop,\n" +
            "    lm.name AS level_merop,\n" +
            "    tm.name AS type_merop,\n" +
            "    COUNT(u.id) AS total_count,\n" +
            "    COUNT(CASE WHEN d.win_place = 'Первое' THEN 1 END) AS num_first,\n" +
            "    COUNT(CASE WHEN d.win_place = 'Второе' THEN 1 END) AS num_second,\n" +
            "    COUNT(CASE WHEN d.win_place = 'Третье' THEN 1 END) AS num_third\n" +
            "FROM\n" +
            "    meropriyatie m\n" +
            "    LEFT JOIN level_meropriyatiya lm ON m.level_id = lm.id\n" +
            "    LEFT JOIN type_meropriyatiya tm ON m.type_id = tm.id\n" +
            "    LEFT JOIN uchastnik u ON m.id = u.meropriyatie_id\n" +
            "    LEFT JOIN dostijenie d ON m.id = d.meropriyatie_id AND u.kid_id = d.kid_id\n" +
            "WHERE\n" +
            "    m.data BETWEEN :d1 AND :d2\n" +
            "GROUP BY\n" +
            "    m.id, m.name, m.data, lm.name, tm.name;",nativeQuery = true)
    List<Object[]> getStatByMeropriyatie(@Param("d1") String date1,
                                         @Param("d2") String date2);
}
