package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Teacher;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeacherRepository extends CrudRepository<Teacher,Long> {

    Teacher findByUsername(String username);

    @Query(value = "SELECT \n" +
            "    teacher.id AS id, CONCAT(teacher.surname, ' ', LEFT(teacher.name, 1), '.', LEFT(teacher.patronymic, 1), '.') AS full_name, \n" +
            "    COUNT(dostijenie.id) AS count_participations, \n" +
            "SUM(CASE WHEN level_meropriyatiya.name = 'Международный' THEN 1 ELSE 0 END) AS count_international_participations,\n" +
            "     SUM(CASE WHEN level_meropriyatiya.name = 'Районный' THEN 1 ELSE 0 END) AS count_rayon_participations,\n" +
            "      SUM(CASE WHEN level_meropriyatiya.name = 'Городской' THEN 1 ELSE 0 END) AS count_city_participations,\n" +
            "       SUM(CASE WHEN level_meropriyatiya.name = 'Региональный' THEN 1 ELSE 0 END) AS count_region_participations,\n" +
            "        SUM(CASE WHEN level_meropriyatiya.name = 'Национальный' THEN 1 ELSE 0 END) AS count_nacion_participations,\n" +
            "    SUM(CASE WHEN meropriyatie.place = 'Первое' THEN 1 ELSE 0 END) AS count_first_places,\n" +
            "    SUM(CASE WHEN meropriyatie.place = 'Второе' THEN 1 ELSE 0 END) AS count_second_places,\n" +
            "    SUM(CASE WHEN meropriyatie.place = 'Третье' THEN 1 ELSE 0 END) AS count_third_places\n" +
            "FROM teacher\n" +
            "LEFT JOIN dostijenie ON teacher.id = dostijenie.teacher_id\n" +
            "LEFT JOIN meropriyatie ON dostijenie.meropriyatie_id = meropriyatie.id\n" +
            "LEFT JOIN level_meropriyatiya ON meropriyatie.level_id = level_meropriyatiya.id\n" +
            "WHERE meropriyatie.data >= :d1 AND meropriyatie.data <= :d2\n"+
            "GROUP BY teacher.id;", nativeQuery = true)
    List<Object[]> getStatByDost(@Param("d1") String date1,
                                 @Param("d2") String date2);



    @Query(value = "SELECT \n" +
            "    COUNT(DISTINCT kid.id) AS count_students\n" +
            "FROM teacher\n" +
            "LEFT JOIN teacher_krujok ON teacher.id = teacher_krujok.teacher_id\n" +
            "LEFT JOIN krujok ON teacher_krujok.krujok_id = krujok.id AND krujok.archive = 0\n" +
            "LEFT JOIN kid_krujok ON krujok.id = kid_krujok.krujok_id\n" +
            "LEFT JOIN kid ON kid_krujok.kid_id = kid.id\n" +
            "WHERE teacher.id = :id\n" +
            "GROUP BY teacher.id;", nativeQuery = true)
    int getNumKidsByTeacher(@Param("id") Long id);


}
