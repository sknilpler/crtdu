package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Krujok;
import com.diplom.crtdu.utils.ThreeIdsModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KrujokRepository extends CrudRepository<Krujok,Long> {
    List<Krujok> findByTeachersId(Long id);

    @Query(value = "SELECT DISTINCT creative_association.id AS ca_id, krujok.id AS kr_id, kid.id AS kid_id\n" +
            "FROM creative_association, krujok, kid, meropriyatie, kid_krujok, kid_meropriyatie, krujok_meropriyatie\n" +
            "WHERE creative_association.id = krujok.creative_association_id AND \n" +
            "krujok.id = krujok_meropriyatie.krujok_id AND\n" +
            "kid.id = kid_krujok.kid_id AND\n" +
            "kid.id = kid_meropriyatie.kid_id AND\n" +
            "krujok_meropriyatie.meropriyatie_id = kid_meropriyatie.meropriyatie_id AND\n" +
            "kid_meropriyatie.meropriyatie_id = :id", nativeQuery = true)
    List<Object[]> findByMeropriyatieId(@Param("id") Long id);

    List<Krujok> findByCreativeAssociationId(Long id);
    @Query(value = "select krujok.* from krujok, kid_krujok where kid_krujok.kid_id = :id and kid_krujok.krujok_id = krujok.id", nativeQuery = true)
    List<Krujok> findByKidId(@Param("id") Long id);

    @Query(value = "select krujok.* from krujok where krujok.type_krujok_id = :id order by krujok.creative_association_id",nativeQuery = true)
    List<Krujok> findByTypeKrujokIdByOrderByCreativeAssociationName(@Param("id") Long id);

    @Query(value = "select krujok.* from krujok order by krujok.creative_association_id, krujok.name", nativeQuery = true)
    List<Krujok> findAllByOrderByCreativeAssociationNameAndKrujokName();


}
