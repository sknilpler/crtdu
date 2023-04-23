package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.Krujok;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KrujokRepository extends CrudRepository<Krujok,Long> {
    List<Krujok> findByTeachersId(Long id);

    List<Krujok> findByCreativeAssociationId(Long id);
}
