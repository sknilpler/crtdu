package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.KidZanyatie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KidZanyatieRepository extends CrudRepository<KidZanyatie, Long> {

    List<KidZanyatie> findByKidId(Long kidId);

    List<KidZanyatie> findByZanyatieId(Long zanyatieId);

    KidZanyatie findByKidIdAndZanyatieId(Long kidId, Long zanyatieId);

    @Query(value = "SELECT kid_zanyatie.* FROM kid_zanyatie, zanyatie\n" +
            "WHERE kid_zanyatie.kid_id = :id1 AND \n" +
            "kid_zanyatie.zanyatie_id = zanyatie.id and zanyatie.krujok_id = :id2", nativeQuery = true)
    List<KidZanyatie> findByKidIdAndKrujokId(@Param("id1") Long kidId, @Param("id2")Long krujokId);

}
