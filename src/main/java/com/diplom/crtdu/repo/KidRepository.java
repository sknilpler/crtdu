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
}
