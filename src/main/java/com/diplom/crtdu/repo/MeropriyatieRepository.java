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
}
