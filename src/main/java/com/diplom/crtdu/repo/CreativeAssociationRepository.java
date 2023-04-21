package com.diplom.crtdu.repo;

import com.diplom.crtdu.models.CreativeAssociation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CreativeAssociationRepository extends CrudRepository<CreativeAssociation,Long> {

    List<CreativeAssociation> findAllByOrderByName();
}
