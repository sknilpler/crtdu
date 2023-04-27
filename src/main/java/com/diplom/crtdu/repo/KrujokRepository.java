package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Krujok;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KrujokRepository extends CrudRepository<Krujok,Long> {
    List<Krujok> findByTeachersId(Long id);

    List<Krujok> findByCreativeAssociationId(Long id);
    @Query(value = "select krujok.* from krujok, kid_krujok where kid_krujok.kid_id = :id and kid_krujok.krujok_id = krujok.id", nativeQuery = true)
    List<Krujok> findByKidId(@Param("id") Long id);

}
